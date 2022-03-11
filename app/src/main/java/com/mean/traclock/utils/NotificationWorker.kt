package com.mean.traclock.utils

import android.content.Context
import android.os.SystemClock
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.mean.traclock.App
import com.mean.traclock.R

private const val NOTIFICATION_ID = 1

class NotificationWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
    override fun doWork(): Result {
        val builder = NotificationCompat.Builder(
            App.context, App.context.getString(R.string.notice_channel_id)
        )
            .setSmallIcon(R.drawable.ic_logo)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)
            .setOngoing(true)
        val manager = NotificationManagerCompat.from(App.context)
        val projectName = inputData.getString("projectName") ?: TimingControl.getProjectName()
        val startTime = inputData.getLong("startTime", TimingControl.getStartTime())
        builder.setContentTitle(projectName)
        while (App.isTiming.value) {
            builder.setContentText(getDurationString(startTime, System.currentTimeMillis()))
            manager.notify(NOTIFICATION_ID, builder.build())
            SystemClock.sleep(1000L)
        }
        builder.setOngoing(false)
        manager.notify(NOTIFICATION_ID, builder.build())
        return Result.success()
    }
}
