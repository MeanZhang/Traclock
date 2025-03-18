package com.mean.traclock.viewmodels

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.mean.traclock.data.repository.ProjectsRepository
import com.mean.traclock.data.repository.RecordWithProjectRepository
import com.mean.traclock.data.repository.RecordsRepository
import com.mean.traclock.model.Project
import com.mean.traclock.model.Record
import com.mean.traclock.utils.openInputStream
import com.mean.traclock.utils.openOutputStream
import io.github.vinceglb.filekit.core.PlatformFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

private const val BACKUP_FILE_VERSION = 1
private const val TABLE_SEPARATOR = "----------"

class BackupRestoreViewModel(
    private val projectsRepo: ProjectsRepository,
    private val recordsRepo: RecordsRepository,
    private val recordWithProjectRepo: RecordWithProjectRepository,
) :
    ViewModel() {
    private val _showConfirmDialog = MutableStateFlow(false)
    private val _backingUp = MutableStateFlow(false)
    private val _showBackupDialog = MutableStateFlow(false)
    private val _showRestoreDialog = MutableStateFlow(false)

    private var restoreFile: PlatformFile? = null
    private val _progress = MutableStateFlow(0F)
    private val _restoreState = MutableStateFlow(RestoreState.RESTORING)
    private val _message = MutableStateFlow("")

    val showConfirmDialog: StateFlow<Boolean>
        get() = _showConfirmDialog
    val backingUp: StateFlow<Boolean>
        get() = _backingUp
    val showBackupDialog: StateFlow<Boolean>
        get() = _showBackupDialog
    val showRestoreDialog: StateFlow<Boolean>
        get() = _showRestoreDialog
    val progress: StateFlow<Float>
        get() = _progress
    val restoreState: StateFlow<RestoreState>
        get() = _restoreState
    val message: StateFlow<String>
        get() = _message

    fun setRestoreFile(file: PlatformFile) {
        restoreFile = file
    }

    fun setShowConfirmDialog(show: Boolean) {
        _showConfirmDialog.value = show
    }

    fun setShowingBackupDialog(show: Boolean) {
        _showBackupDialog.value = show
    }

    private fun setErrorMessage(message: String) {
        _message.value = message
        Logger.e(message)
    }

    private fun setProgress(progress: Float) {
        _progress.value = if (progress > 1F) 1F else progress
    }

    fun setShowRestoreDialog(show: Boolean) {
        _showRestoreDialog.value = show
    }

    fun backup(file: PlatformFile) {
        setProgress(0F)
        _showBackupDialog.value = true
        _backingUp.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val recordsWithProject = recordWithProjectRepo.getAll()
            val projects = projectsRepo.projects.first()
            val size = projects.size + recordsWithProject.size
            var completed = 0
            file.openOutputStream().use { outputStream ->
                BufferedWriter(OutputStreamWriter(outputStream)).use { writer ->
                    writer.write("Version: $BACKUP_FILE_VERSION\n")
                    writer.write("Project ID, Project Name, Project Color\n")
                    for (project in projects) {
                        writer.write("${project.id}, ${project.name}, ${project.color.toArgb()}\n")
                        setProgress(completed++.toFloat() / size)
                    }
                    writer.write(TABLE_SEPARATOR + "\n")
                    writer.write("Record ID, Project ID, Start Time, End Time\n")
                    for (recordWithProject in recordsWithProject) {
                        writer.write(
                            "${recordWithProject.record.id}, ${recordWithProject.record.projectId}, " +
                                "${recordWithProject.record.startTime.toEpochMilliseconds()}, " +
                                "${recordWithProject.record.endTime.toEpochMilliseconds()}\n",
                        )
                        setProgress(completed++.toFloat() / size)
                    }
                }
            }
            setProgress(1F)
            _backingUp.value = false
        }
    }

    fun restore() {
        setProgress(0F)
        _showRestoreDialog.value = true
        _restoreState.value = RestoreState.RESTORING
        var restoredBytesSize = 0L
        var lineIndex = 2
        restoreFile?.let { file ->
            viewModelScope.launch(Dispatchers.IO) {
                val totalBytes = file.getSize()
                if (totalBytes == null) {
                    _restoreState.value = RestoreState.FAILED
                    return@launch
                }
                file.openInputStream()?.use { inputStream ->
                    BufferedReader(InputStreamReader(inputStream)).use { reader ->
                        var line = reader.readLine()
                        restoredBytesSize += line.toByteArray().size + 1
                        val version =
                            try {
                                line.split(":")[1].trim().toInt()
                            } catch (_: Exception) {
                                null
                            }
                        if (version != null) {
                            reader.readLine()
                        }
                        line = reader.readLine()
                        while (line != null && line != TABLE_SEPARATOR) {
                            val res = restoreProject(line, lineIndex)
                            if (res != RestoreError.NO_ERROR) {
                                setProgress(1F)
                                _restoreState.value = RestoreState.FAILED
                                return@launch
                            }
                            lineIndex++
                            restoredBytesSize += line.toByteArray().size + 1
                            setProgress(restoredBytesSize.toFloat() / totalBytes)

                            line = reader.readLine()
                        }
                        reader.readLine()
                        line = reader.readLine()
                        while (line != null) {
                            val res = restoreRecord(line, lineIndex)
                            if (res != RestoreError.NO_ERROR) {
                                setProgress(1F)
                                _restoreState.value = RestoreState.FAILED
                                return@launch
                            }
                            lineIndex++
                            restoredBytesSize += line.toByteArray().size + 1
                            setProgress(restoredBytesSize.toFloat() / totalBytes)

                            line = reader.readLine()
                        }
                    }
                }
                setProgress(1F)
                _restoreState.value = RestoreState.SUCCESS
            }
        }
    }

    /**
     * 恢复一行项目
     *
     * 每行数据均为 3 列（[Long], [String], [Long]），分别为：项目ID，项目名，颜色
     */
    private suspend fun restoreProject(
        line: String,
        lineIndex: Int,
    ): RestoreError {
        /**
         * 每行数据均为 3 列（[Long], [String], [Long]），分别为：项目ID，项目名，颜色
         */
        val columns = line.split(",").map { it.trim() }
        if (columns.size != 3) {
            // 列数错误
            setErrorMessage("第${lineIndex}行列数错误：应为3列，实为${columns.size}列")
            return RestoreError.COLUMN_ERROR
        }

        /** -1或开始时间，第二列 */
        val projectId =
            try {
                columns[0].toLong()
            } catch (e: Exception) {
                setErrorMessage("第${lineIndex}行项目ID格式有误：${columns[0]}")
                return RestoreError.START_TIME_ERROR
            }

        /** 项目名，第二列 */
        val projectName = columns[1]
        if (projectName.isBlank()) {
            // 项目名为空
            setErrorMessage("第${lineIndex}行项目名为空")
            return RestoreError.PROJECT_NAME_EMPTY
        }

        /** 颜色，第三列 */
        val color =
            try {
                Color(columns[2].toInt())
            } catch (e: Exception) {
                setErrorMessage("第${lineIndex}行颜色格式有误：${columns[2]}")
                return RestoreError.COLOR_ERROR
            }

        try {
            projectsRepo.insert(Project(projectName, color))
        } catch (_: Exception) {
            Logger.w { "项目${projectName}已存在" }
        }

        return RestoreError.NO_ERROR
    }

    /**
     * 恢复一行记录
     *
     * 每行数据均为 4 列（[Long], [Long], [Long], [Long]），分别为：记录ID，项目ID，开始时间，结束时间
     */
    private suspend fun restoreRecord(
        line: String,
        lineIndex: Int,
    ): RestoreError {
        /**
         * 每行数据均为 4 列（[Long], [Long], [Long], [Long]），分别为：记录ID，项目ID，开始时间，结束时间
         */
        val columns = line.split(",").map { it.trim() }
        if (columns.size != 4) {
            // 列数错误
            setErrorMessage("第${lineIndex}行列数错误：应为4列，实为${columns.size}列")
            return RestoreError.COLUMN_ERROR
        }

        /** 记录ID，第一列 */
        val recordId =
            try {
                columns[0].toLong()
            } catch (e: Exception) {
                setErrorMessage("第${lineIndex}行记录ID格式有误：${columns[0]}")
                return RestoreError.RECORD_ID_ERROR
            }

        /** 项目ID，第二列 */
        val projectId =
            try {
                columns[1].toLong()
            } catch (e: Exception) {
                setErrorMessage("第${lineIndex}行项目ID格式有误：${columns[1]}")
                return RestoreError.PROJECT_ID_ERROR
            }
        val startTime =
            try {
                columns[2].toLong()
            } catch (e: Exception) {
                setErrorMessage("第${lineIndex}行开始时间格式有误：${columns[2]}")
                return RestoreError.START_TIME_ERROR
            }
        val endTime =
            try {
                columns[3].toLong()
            } catch (e: Exception) {
                setErrorMessage("第${lineIndex}行结束时间格式有误：${columns[3]}")
                return RestoreError.END_TIME_ERROR
            }
        val date = Instant.fromEpochMilliseconds(startTime).toLocalDateTime(TimeZone.currentSystemDefault()).date
        val record =
            Record(
                projectId,
                Instant.fromEpochMilliseconds(startTime),
                Instant.fromEpochMilliseconds(endTime),
                date,
            )
        try {
            recordsRepo.insert(record)
        } catch (e: Exception) {
            Logger.e(e) { "第${lineIndex}行记录插入失败：$record" }
            setErrorMessage("第${lineIndex}行记录插入失败：$record")
            return RestoreError.INSERT_ERROR
        }
        return RestoreError.NO_ERROR
    }

    enum class RestoreState {
        /** 恢复中 */
        RESTORING,

        /** 恢复成功 */
        SUCCESS,

        /** 恢复失败 */
        FAILED,
    }

    enum class RestoreError {
        COLUMN_ERROR,
        RECORD_ID_ERROR,
        PROJECT_ID_ERROR,
        START_TIME_ERROR,
        END_TIME_ERROR,
        COLOR_ERROR,
        INSERT_ERROR,
        NO_ERROR,
        PROJECT_NAME_EMPTY,
    }
}
