package com.mean.traclock.utils

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.mean.traclock.App
import com.mean.traclock.MainActivity
import com.mean.traclock.R
import java.lang.Thread.sleep

/** 用于发送通知 */
class NotificationWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
    private val projectName =
        inputData.getString("projectName") ?: TimingControl.getProjectName()
    private val startTime = inputData.getLong("startTime", TimingControl.getStartTime())
    private val manager = NotificationManagerCompat.from(App.context)
    private val notificationBuilder = NotificationCompat.Builder(
        App.context, Config.TIMING_NOTIFICATION_CHANNEL
    )
        .setSmallIcon(R.drawable.ic_logo)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setOnlyAlertOnce(true)
        .setContentIntent(
            PendingIntent.getActivity(
                App.context,
                0,
                Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER).setComponent(
                    ComponentName(App.context, MainActivity::class.java)
                ),
                PendingIntent.FLAG_IMMUTABLE
            )
        )

    @SuppressLint("RestrictedApi")
    override fun doWork(): Result {
        val actionIntent = Intent(Config.NOTIFICATION_ACTION).putExtra("isTiming", false)
        val actionPendingIntent =
            PendingIntent.getBroadcast(App.context, 0, actionIntent, PendingIntent.FLAG_IMMUTABLE)

        notificationBuilder.setOngoing(true) // 设置通知不可清除
        // 添加停止按钮
        notificationBuilder.clearActions().addAction(
            R.drawable.ic_logo,
            App.context.getString(R.string.stop),
            actionPendingIntent
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
            manager.notify(Config.TIMING_NOTIFICATION_ID, notificationBuilder.build())
            sleep(1000L)
        }
        return Result.success()
    }

    override fun onStopped() {
        super.onStopped()
        notificationBuilder
            .setOngoing(false) // 设置通知可清除
            .setContentTitle(projectName)
            .setContentText(getDurationString(startTime, System.currentTimeMillis()))

        val actionIntent = Intent(Config.NOTIFICATION_ACTION).putExtra("isTiming", true)
            .putExtra("projectName", projectName)
        val actionPendingIntent =
            PendingIntent.getBroadcast(
                App.context,
                1,
                actionIntent,
                PendingIntent.FLAG_IMMUTABLE
            )

        // 添加开始按钮
        notificationBuilder.clearActions().addAction(
            R.drawable.ic_logo,
            App.context.getString(R.string.start),
            actionPendingIntent
        )
        manager.notify(Config.TIMING_NOTIFICATION_ID, notificationBuilder.build())
    }
}
