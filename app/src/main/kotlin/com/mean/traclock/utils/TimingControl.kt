package com.mean.traclock.utils

import android.widget.Toast
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.mean.traclock.App
import com.mean.traclock.App.Companion.context
import com.mean.traclock.R
import com.mean.traclock.dataStore
import com.mean.traclock.database.Record
import com.orhanobut.logger.Logger

/**
 * 计时控制
 */
object TimingControl {
    private val workManager = WorkManager.getInstance(context)
    private val workRequest = OneTimeWorkRequest.Builder(NotificationWorker::class.java).build()

    /**
     * 开始记录，同时开始发送通知
     * @param projectName 记录的项目名
     */
    suspend fun startRecord(projectName: String) {
        if (App.isTiming.value) {
            Logger.d("项目%s正在记录，请先停止", App.projectName.value)
            Toast.makeText(context, context.getString(R.string.is_tracking), Toast.LENGTH_SHORT)
                .show()
        } else {
            val startTime = System.currentTimeMillis()
            setIsTiming(true)
            setProjectName(projectName)
            setStartTime(startTime)
            Logger.d("项目%s开始记录", App.projectName.value)
            startNotify()
        }
    }

    /**
     * 停止记录，同时停止发送通知，并将记录保存在数据库中
     */
    suspend fun stopRecord() {
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

    /**
     * 开始发送通知
     */
    fun startNotify() {
        workManager.enqueueUniqueWork(
            Config.NOTIFICATION_WORK_NAME,
            ExistingWorkPolicy.REPLACE,
            workRequest
        )
    }

    private suspend fun setIsTiming(isTiming: Boolean) {
        App.isTiming.value = isTiming
        Logger.d("设置isTiming为%s", isTiming)
        context.dataStore.edit { it[booleanPreferencesKey("isTiming")] = isTiming }
    }

    private suspend fun setProjectName(projectName: String) {
        App.projectName.value = projectName
        context.dataStore.edit { it[stringPreferencesKey("projectName")] = projectName }
    }

    private suspend fun setStartTime(startTime: Long) {
        App.startTime.value = startTime
        context.dataStore.edit { it[longPreferencesKey("startTime")] = startTime }
    }
}
