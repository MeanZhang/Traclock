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
    private val projectName = inputData.getString("projectName") ?: TimingControl.getProjectName()
    private val startTime = inputData.getLong("startTime", TimingControl.getStartTime())
    private val manager = NotificationManagerCompat.from(App.context)
    private val notificationBuilder = NotificationCompat.Builder(
        App.context, App.context.getString(R.string.timing_channel_id)
    )
        .setSmallIcon(R.drawable.ic_logo)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setAutoCancel(true)
        .setOnlyAlertOnce(true)

    @SuppressLint("RestrictedApi")
    override fun doWork(): Result {
        val intent = Intent(NOTIFICATION_ACTION).putExtra("isTiming", false)
        val pendingIntent =
            PendingIntent.getBroadcast(App.context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        notificationBuilder.setOngoing(true)
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
        this.stop()
        return Result.success()
    }

    override fun onStopped() {
        super.onStopped()
        val intent = Intent(NOTIFICATION_ACTION).putExtra("isTiming", true)
            .putExtra("projectName", projectName)
        val pendingIntent =
            PendingIntent.getBroadcast(App.context, 1, intent, PendingIntent.FLAG_IMMUTABLE)

        notificationBuilder.setOngoing(false) // 设置通知可清除
            .setContentTitle(projectName)
            .setContentText(getDurationString(startTime, System.currentTimeMillis()))
        // 添加开始按钮
        notificationBuilder.clearActions().addAction(
            R.drawable.ic_logo,
            App.context.getString(R.string.start),
            pendingIntent
        )
        manager.notify(TIMING_NOTIFICATION_ID, notificationBuilder.build())
    }
}
