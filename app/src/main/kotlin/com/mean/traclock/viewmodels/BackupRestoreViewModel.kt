package com.mean.traclock.viewmodels

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mean.traclock.App
import com.mean.traclock.database.AppDatabase
import com.mean.traclock.database.Project
import com.mean.traclock.database.Record
import com.mean.traclock.utils.Database
import com.mean.traclock.utils.TLog
import com.mean.traclock.utils.getIntDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter

class BackupRestoreViewModel : ViewModel() {
    val showConfirmDialog = MutableStateFlow(false)
    val backingUp = MutableStateFlow(false)
    val restoring = MutableStateFlow(false)
    val showBackupDialog = MutableStateFlow(false)
    val showRestoreDialog = MutableStateFlow(false)
    private var restoreUri: Uri? = null
    val progress = MutableStateFlow(0F)

    fun setRestoreUri(uri: Uri) {
        restoreUri = uri
    }

    fun backup(uri: Uri) {
        progress.value = 0F
        showBackupDialog.value = true
        backingUp.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val records = AppDatabase.getDatabase(App.context).recordDao().getRecordsList()
            val size = App.projects.size + records.size
            var done = 0
            App.context.contentResolver.openOutputStream(uri).use { outputStream ->
                BufferedWriter(OutputStreamWriter(outputStream)).use { writer ->
                    writer.write("Project,Start Time,End Time\n")
                    for (project in App.projects) {
                        writer.write(project.key + ",-1," + project.value + "\n")
                        progress.value = done++.toFloat() / size
                    }
                    for (record in records) {
                        writer.write(record.project + "," + record.startTime + "," + record.endTime + "\n")
                        progress.value = done++.toFloat() / size
                    }
                }
            }
            progress.value = 1F
            backingUp.value = false
        }
    }

    fun restore() {
        progress.value = 0F
        showRestoreDialog.value = true
        restoring.value = true
        var lines = 1
        var done = 0
        restoreUri?.let { uri ->
            viewModelScope.launch(Dispatchers.IO) {
                App.context.contentResolver.openInputStream(uri)?.use { inputStream ->
                    BufferedReader(InputStreamReader(inputStream)).use {
                        lines = it.readLines().size
                    }
                }
                App.context.contentResolver.openInputStream(uri)?.use { inputStream ->
                    BufferedReader(InputStreamReader(inputStream)).use { reader ->
                        reader.readLine() // ???????????????
                        done++
                        var line = reader.readLine()
                        while (line != null) {
                            val res = restore(line)
                            progress.value = done++.toFloat() / lines
                            if (res < 0) {
                                break
                            }
                            line = reader.readLine()
                        }
                    }
                }
                progress.value = 1F
                restoring.value = false
            }
        }
    }

    private fun restore(line: String): Int {
        val columns = line.split(",")
        // ?????? -1 ??????
        // ?????? ???????????????2000-01-01 00:00:00??? ????????????
        if (columns.size < 3) {
            return -1 // ????????????
        }
        val projectName = columns[0]
        if (projectName.isBlank()) {
            return -2 // ???????????????
        }
        val startTime = try {
            columns[1].toLong()
        } catch (e: Exception) {
            TLog.e(e)
            return -3
        }
        if (startTime == -1L) {
            val color = try {
                columns[2].toInt()
            } catch (e: Exception) {
                TLog.e(e)
                return -3
            }
            Database.insertProject(Project(projectName, color))
        } else {
            val date = getIntDate(startTime)
            val endTime = try {
                columns[2].toLong()
            } catch (e: Exception) {
                return -4
            }
            val record = Record(projectName, startTime, endTime, date)
            if (!Database.insertRecord(record)) {
                return -4
            }
        }
        return 0
    }
}
