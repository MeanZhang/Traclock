package com.mean.traclock.utils

import android.widget.Toast
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.mean.traclock.App
import com.mean.traclock.App.Companion.context
import com.mean.traclock.R
import com.mean.traclock.database.Record
import com.orhanobut.logger.Logger
import com.tencent.mmkv.MMKV

object TimingControl {
    private val kv = MMKV.defaultMMKV()
    private val workManager = WorkManager.getInstance(context)
    private val workRequest = OneTimeWorkRequest.Builder(NotificationWorker::class.java).build()

    fun startRecord(project: String) {
        if (App.isTiming.value) {
            Logger.d("项目%s正在记录，请先停止", App.projectName.value)
            Toast.makeText(context, context.getString(R.string.is_tracking), Toast.LENGTH_SHORT)
                .show()
        } else {
            val startTime = System.currentTimeMillis()
            setIsTiming(true)
            setProjectName(project)
            setStartTime(startTime)
            Logger.d("项目%s开始记录", App.projectName.value)
            startNotify()
        }
    }

    fun stopRecord() {
        if (App.isTiming.value) {
            setIsTiming(false)
            workManager.cancelUniqueWork(Config.NOTIFICATION_WORK_NAME)
            Logger.d("项目%s停止记录", App.projectName.value)
            val record = Record(
                App.projectName.value,
                App.startTime.value,
                System.currentTimeMillis()
            )
            Database.insertRecord(record)
        }
    }

    fun startNotify() {
        workManager.enqueueUniqueWork(
            Config.NOTIFICATION_WORK_NAME,
            ExistingWorkPolicy.REPLACE,
            workRequest
        )
    }

    private fun setIsTiming(isTiming: Boolean) {
        App.isTiming.value = isTiming
        kv.encode("isTiming", isTiming)
    }

    private fun setProjectName(projectName: String) {
        App.projectName.value = projectName
        kv.encode("projectName", projectName)
    }

    private fun setStartTime(startTime: Long) {
        App.startTime.value = startTime
        kv.encode("startTime", startTime)
    }
}
