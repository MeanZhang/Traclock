package com.mean.traclock.utils

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.mean.traclock.App
import com.mean.traclock.App.Companion.context
import com.mean.traclock.R
import com.mean.traclock.database.Record

object TimingControl {
    private val sharedPref = context.getSharedPreferences(
        context.getString(R.string.preference_file_key),
        Context.MODE_PRIVATE
    )

    fun startRecord(project: String) {
        if (App.isTiming.value) {
            Toast.makeText(context, context.getString(R.string.is_tracking), Toast.LENGTH_SHORT)
                .show()
        } else {
            App.isTiming.value = true
            val startTime = System.currentTimeMillis()
            with(sharedPref.edit()) {
                putBoolean("isTiming", true)
                putString("projectName", project)
                putLong("startTime", startTime)
                apply()
            }
            startNotify(project)
        }
    }

    fun stopRecord() {
        if (App.isTiming.value) {
            App.isTiming.value = false
            with(sharedPref.edit()) {
                putBoolean("isTiming", false)
                apply()
            }
            val record = Record(
                getProjectName(),
                getStartTime(),
                System.currentTimeMillis()
            )
            Database.insertRecord(record)

            //发送通知
            val manager = NotificationManagerCompat.from(context)
            val notificationBuilder = NotificationCompat.Builder(
                context, context.getString(R.string.timing_channel_id)
            )
                .setSmallIcon(R.drawable.ic_logo)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .setOngoing(false) // 设置通知可清除
                .setContentTitle(record.project)
                .setContentText(getDurationString(record.startTime, record.endTime))

            val intent = Intent(NOTIFICATION_ACTION).putExtra("isTiming", true)
                .putExtra("projectName", record.project)
            val pendingIntent =
                PendingIntent.getBroadcast(context, 1, intent, PendingIntent.FLAG_IMMUTABLE)

            // 添加开始按钮
            notificationBuilder.addAction(
                R.drawable.ic_logo,
                context.getString(R.string.start),
                pendingIntent
            )
            manager.notify(TIMING_NOTIFICATION_ID, notificationBuilder.build())
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
            NOTIFICATION_WORK_NAME,
            ExistingWorkPolicy.REPLACE,
            request
        )
    }

    fun getIsTiming() = sharedPref.getBoolean("isTiming", false)

    fun getProjectName() = sharedPref.getString("projectName", "") ?: ""

    fun getStartTime() = sharedPref.getLong("startTime", System.currentTimeMillis())
}
