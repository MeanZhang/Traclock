package com.mean.traclock.utils

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.mean.traclock.App
import com.mean.traclock.R
import java.lang.Thread.sleep

/** 用于发送通知 */
class NotificationWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
    @SuppressLint("RestrictedApi")
    override fun doWork(): Result {
        val projectName =
            inputData.getString("projectName") ?: TimingControl.getProjectName()
        val startTime = inputData.getLong("startTime", TimingControl.getStartTime())
        val manager = NotificationManagerCompat.from(App.context)
        val notificationBuilder = NotificationCompat.Builder(
            App.context, App.context.getString(R.string.timing_channel_id)
        )
            .setSmallIcon(R.drawable.ic_logo)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)
            .setOngoing(true)

        val intent = Intent(NOTIFICATION_ACTION).putExtra("isTiming", false)
        val pendingIntent =
            PendingIntent.getBroadcast(App.context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        // 添加停止按钮
        notificationBuilder.addAction(
            R.drawable.ic_logo,
            App.context.getString(R.string.stop),
            pendingIntent
        )
        notificationBuilder.setContentTitle(projectName) // 设置通知标题（项目名）
        while (App.isTiming.value && !this.isStopped) {
            // 设置通知内容（记录时间）
            notificationBuilder.setContentText(
                getDurationString(
                    startTime,
                    System.currentTimeMillis()
                )
            )
            manager.notify(TIMING_NOTIFICATION_ID, notificationBuilder.build())
            sleep(1000L)
        }
        return Result.success()
    }
}
