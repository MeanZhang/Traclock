package com.mean.traclock.utils

import android.widget.Toast
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.mean.traclock.App
import com.mean.traclock.App.Companion.context
import com.mean.traclock.R
import com.mean.traclock.database.Record
import com.tencent.mmkv.MMKV

object TimingControl {
    private val kv = MMKV.defaultMMKV()

    fun startRecord(project: String) {
        if (App.isTiming.value) {
            Toast.makeText(context, context.getString(R.string.is_tracking), Toast.LENGTH_SHORT)
                .show()
        } else {
            App.isTiming.value = true
            val startTime = System.currentTimeMillis()
            kv.encode("isTiming", true)
            kv.encode("projectName", project)
            kv.encode("startTime", startTime)
            startNotify(project)
        }
    }

    fun stopRecord() {
        if (App.isTiming.value) {
            App.isTiming.value = false
            kv.encode("isTiming", false)
            val record = Record(
                getProjectName(),
                getStartTime(),
                System.currentTimeMillis()
            )
            Database.insertRecord(record)

            WorkManager.getInstance(context).cancelUniqueWork(Config.NOTIFICATION_WORK_NAME)
        }
    }

    fun startNotify(projectName: String = getProjectName(), startTime: Long = getStartTime()) {
        val workManager = WorkManager.getInstance(context)
        val data =
            Data.Builder()
                .putString("projectName", projectName)
                .putLong("startTime", startTime)
                .build()
        val request =
            OneTimeWorkRequest.Builder(NotificationWorker::class.java).setInputData(data)
                .build()
        workManager.enqueueUniqueWork(
            Config.NOTIFICATION_WORK_NAME,
            ExistingWorkPolicy.REPLACE,
            request
        )
    }

    fun getIsTiming() = kv.decodeBool("isTiming", false)

    fun getProjectName() = kv.decodeString("projectName", "") ?: ""

    fun getStartTime() = kv.decodeLong("startTime", System.currentTimeMillis())
}
