package com.mean.traclock.viewmodels

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.mean.traclock.App
import com.mean.traclock.R
import com.mean.traclock.database.AppDatabase
import com.mean.traclock.utils.RestoreWorker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.io.BufferedWriter
import java.io.OutputStreamWriter

class BackupRestoreViewModel : ViewModel() {
    val showConfirmDialog = MutableStateFlow(false)
    val backingUp = MutableStateFlow(false)
    val showBackupDialog = MutableStateFlow(false)
    private var restoreUri: Uri? = null
    val backupProcess = MutableStateFlow(0F)

    fun setRestoreUri(uri: Uri) {
        restoreUri = uri
    }

    fun backup(uri: Uri) {
        showBackupDialog.value = true
        backingUp.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val records = AppDatabase.getDatabase(App.context).recordDao().getRecordsList()
            val size = App.projectsList.size + records.size
            var done = 0
            App.context.contentResolver.openOutputStream(uri).use { outputStream ->
                BufferedWriter(OutputStreamWriter(outputStream)).use { writer ->
                    writer.write("Project,Start Time,End Time\n")
                    for (project in App.projectsList) {
                        writer.write(project.key + ",-1," + project.value + "\n")
                        backupProcess.value = done++.toFloat() / size
                    }
                    for (record in records) {
                        writer.write(record.project + "," + record.startTime + "," + record.endTime + "\n")
                        done++
                        backupProcess.value = done++.toFloat() / size
                    }
                }
            }
            backupProcess.value = 1.0F
            backingUp.value = false
        }
    }
    fun restore() {
        val data =
            Data.Builder().putString("uri", restoreUri.toString())
                .build()
        val request =
            OneTimeWorkRequest.Builder(RestoreWorker::class.java).setInputData(data)
                .build()
        WorkManager.getInstance(App.context)
            .enqueueUniqueWork(
                App.context.getString(R.string.restore_work_name),
                ExistingWorkPolicy.KEEP,
                request
            )
    }
}
