package com.mean.traclock.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.mean.traclock.data.Record
import com.mean.traclock.data.repository.ProjectsRepository
import com.mean.traclock.data.repository.RecordsRepository
import com.mean.traclock.utils.TimeUtils
import com.mean.traclock.utils.openInputStream
import com.mean.traclock.utils.openOutputStream
import data.Project
import io.github.vinceglb.filekit.core.PlatformFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

class BackupRestoreViewModel(
    private val projectsRepo: ProjectsRepository,
    private val recordsRepo: RecordsRepository,
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
    private val projects = projectsRepo.projects

    private val projectToId =
        projects.values.associate { it.name to it.id }.toMutableMap()

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
            val records = recordsRepo.getRecordsList()
            val size = projects.size + records.size
            var completed = 0
            file.openOutputStream().use { outputStream ->
                BufferedWriter(OutputStreamWriter(outputStream)).use { writer ->
                    writer.write("Project, Start Time, End Time\n")
                    for (project in projects) {
                        writer.write(project.value.name + ",-1," + project.value.color + "\n")
                        setProgress(completed++.toFloat() / size)
                    }
                    for (record in records) {
                        writer.write(
                            projects[record.project]!!.name + "," + record.startTime + "," + record.endTime + "\n",
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
                        restoredBytesSize += reader.readLine().toByteArray().size + 1
                        var line = reader.readLine()
                        while (line != null) {
                            val res = restore(line, lineIndex)
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
     * 恢复一行数据
     *
     * 每行数据均为三列（[String], [Long], [Long]），有两种格式：
     * - [Project]：项目名,-1,颜色
     * - [Record]：项目名,开始时间,结束时间
     */
    private suspend fun restore(
        line: String,
        lineIndex: Int,
    ): RestoreError {
        /**
         * 每行数据均为三列（[String], [Long], [Long]），有两种格式：
         * - [Project]：项目名,-1,颜色
         * - [Record]：项目名,开始时间,结束时间
         */
        val columns = line.split(",")
        if (columns.size != 3) {
            // 列数错误
            setErrorMessage("第${lineIndex}行列数错误：应为3列，实为${columns.size}列")
            return RestoreError.COLUMN_ERROR
        }

        /** 项目名，第一列 */
        val projectName = columns[0]
        if (projectName.isBlank()) {
            // 项目名为空
            setErrorMessage("第${lineIndex}行项目名为空")
            return RestoreError.PROJECT_NAME_EMPTY
        }

        /** -1或开始时间，第二列 */
        val startTime =
            try {
                columns[1].toLong()
            } catch (e: Exception) {
                setErrorMessage("第${lineIndex}行开始时间格式有误：${columns[1]}")
                return RestoreError.START_TIME_ERROR
            }
        // 项目
        if (startTime == -1L) {
            val color =
                try {
                    columns[2].toInt()
                } catch (e: Exception) {
                    setErrorMessage("第${lineIndex}行颜色格式有误：${columns[2]}")
                    return RestoreError.COLOR_ERROR
                }
            try {
                val id = projectsRepo.insert(Project(projectName, color))
                projectToId[projectName] = id
            } catch (_: Exception) {
                Logger.w { "项目${projectName}已存在" }
            }
        } else {
            // 记录
            val date = TimeUtils.getIntDate(startTime)
            val endTime =
                try {
                    columns[2].toLong()
                } catch (e: Exception) {
                    setErrorMessage("第${lineIndex}行结束时间格式有误：${columns[2]}")
                    return RestoreError.END_TIME_ERROR
                }
            val record = Record(projectToId[projectName]!!, startTime, endTime, date)
            try {
                recordsRepo.insert(record)
            } catch (e: Exception) {
                setErrorMessage("第${lineIndex}行记录插入失败：$record")
                return RestoreError.INSERT_ERROR
            }
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
        PROJECT_NAME_EMPTY,
        START_TIME_ERROR,
        END_TIME_ERROR,
        COLOR_ERROR,
        INSERT_ERROR,
        NO_ERROR,
    }
}
