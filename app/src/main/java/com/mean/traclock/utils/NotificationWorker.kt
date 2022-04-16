package com.mean.traclock.utils

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.mean.traclock.App
import com.mean.traclock.R

/** 用于发送通知 */
class NotificationWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
    override fun doWork(): Result {
        var intent = Intent(NOTIFICATION_ACTION).putExtra("isTiming", false)
        var pendingIntent =
            PendingIntent.getBroadcast(App.context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val builder = NotificationCompat.Builder(
            App.context, App.context.getString(R.string.timing_channel_id)
        )
            .setSmallIcon(R.drawable.ic_logo)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)
            .setOngoing(true)
            .addAction(
                R.drawable.ic_logo,
                App.context.getString(R.string.stop),
                pendingIntent
            ) // 添加停止按钮
        val manager = NotificationManagerCompat.from(App.context)
        val projectName = inputData.getString("projectName") ?: TimingControl.getProjectName()
        val startTime = inputData.getLong("startTime", TimingControl.getStartTime())
        builder.setContentTitle(projectName) // 设置通知标题（项目名）
        while (App.isTiming.value) {
            // 设置通知内容（记录时间）
            builder.setContentText(getDurationString(startTime, System.currentTimeMillis()))
            manager.notify(TIMING_NOTIFICATION_ID, builder.build())
            SystemClock.sleep(1000L)
        }

        intent = intent.putExtra("isTiming", true)
            .putExtra("projectName", projectName)
        pendingIntent =
            PendingIntent.getBroadcast(App.context, 1, intent, PendingIntent.FLAG_IMMUTABLE)

        builder.clearActions() // 清除按钮
        builder.setOngoing(false) // 设置通知可清除
        // 添加开始按钮
        builder.addAction(R.drawable.ic_logo, App.context.getString(R.string.start), pendingIntent)
        manager.notify(TIMING_NOTIFICATION_ID, builder.build())
        return Result.success()
    }
}
