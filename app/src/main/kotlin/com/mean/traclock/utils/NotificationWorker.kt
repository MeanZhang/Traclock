package com.mean.traclock.utils

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.elvishew.xlog.XLog
import com.mean.traclock.App
import com.mean.traclock.MainActivity
import com.mean.traclock.R
import java.lang.Thread.sleep

/** 用于发送通知 */
class NotificationWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
    private val manager = NotificationManagerCompat.from(App.context)
    private val flag =
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else PendingIntent.FLAG_UPDATE_CURRENT
    private val notificationBuilder = NotificationCompat.Builder(
        App.context,
        Config.TIMING_NOTIFICATION_CHANNEL
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
                flag
            )
        )

    @SuppressLint("RestrictedApi")
    override fun doWork(): Result {
        val actionIntent = Intent(Config.NOTIFICATION_ACTION).putExtra("isTiming", false)
        val actionPendingIntent =
            PendingIntent.getBroadcast(App.context, 0, actionIntent, flag)

        notificationBuilder.setOngoing(true) // 设置通知不可清除
        // 添加停止按钮
        notificationBuilder.clearActions().addAction(
            R.drawable.ic_logo,
            App.context.getString(R.string.stop),
            actionPendingIntent
        )
        XLog.d("开始发送通知")
        while (App.isTiming.value && !this.isStopped) {
            // 设置通知内容（记录时间）
            notificationBuilder
                .setContentTitle(App.projectName.value) // 设置通知标题（项目名）
                .setContentText(
                    getDurationString(
                        App.startTime.value,
                        System.currentTimeMillis()
                    )
                )
            manager.notify(Config.TIMING_NOTIFICATION_ID, notificationBuilder.build())
            sleep(1000L)
        }
        XLog.d("停止发送通知")
        return Result.success()
    }

    override fun onStopped() {
        super.onStopped()
        notificationBuilder
            .setOngoing(false) // 设置通知可清除
            .setContentTitle(App.projectName.value)
            .setContentText(getDurationString(App.startTime.value, System.currentTimeMillis()))

        val actionIntent = Intent(Config.NOTIFICATION_ACTION).putExtra("isTiming", true)
            .putExtra("projectName", App.projectName.value)
        val actionPendingIntent =
            PendingIntent.getBroadcast(
                App.context,
                1,
                actionIntent,
                flag
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
