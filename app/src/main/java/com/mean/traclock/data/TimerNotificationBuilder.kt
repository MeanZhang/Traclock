package com.mean.traclock.data

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.SystemClock
import android.view.View.VISIBLE
import android.widget.RemoteViews
import androidx.annotation.DrawableRes
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.Action
import androidx.core.app.NotificationCompat.Builder
import androidx.core.app.NotificationManagerCompat
import com.elvishew.xlog.XLog
import com.mean.traclock.R
import com.mean.traclock.timer.TimerService
import com.mean.traclock.utils.TimeUtils
import com.mean.traclock.utils.Utils

/** 构建计时通知 */
internal class TimerNotificationBuilder {
    fun buildChannel(context: Context, notificationManager: NotificationManagerCompat) {
        val channel = NotificationChannel(
            TIMER_NOTIFICATION_CHANNEL_ID,
            context.getString(R.string.default_label),
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)
    }

    @SuppressLint("LaunchActivityFromNotification")
    fun build(
        context: Context,
        projectName: String,
        isRunning: Boolean,
        startTime: Long
    ): Notification {
        XLog.d(isRunning)
        /** 点击通知时打开应用的 Intent */
        val showApp: Intent = Intent(context, TimerService::class.java)
            .setAction(TimerService.ACTION_SHOW_APP)

        val pendingShowApp = Utils.pendingServiceIntent(context, showApp)

        val pname: String = context.packageName
        val res: Resources = context.resources
        val base: Long =
            SystemClock.elapsedRealtime() - (TimeUtils.now() - startTime)

        val content = RemoteViews(pname, R.layout.chronometer_notif_content)
        content.setChronometer(R.id.chronometer, base, null, isRunning)

        val actions: MutableList<Action> = ArrayList(2)

        if (isRunning) {
            // 停止按钮
            val stop: Intent = Intent(context, TimerService::class.java)
                .setAction(TimerService.ACTION_STOP_TIMER)

            @DrawableRes val iconStop: Int = R.drawable.ic_logo
            val titleStop: CharSequence = res.getText(R.string.stop)
            val intentStop: PendingIntent = Utils.pendingServiceIntent(context, stop)
            actions.add(Action.Builder(iconStop, titleStop, intentStop).build())

            content.setTextViewText(R.id.project_name, projectName)
            content.setViewVisibility(R.id.project_name, VISIBLE)
        } else {
            // 开始按钮
            val start: Intent = Intent(context, TimerService::class.java)
                .setAction(TimerService.ACTION_START_TIMER)

            @DrawableRes val iconStart: Int = R.drawable.ic_logo
            val titleStart: CharSequence = res.getText(R.string.start)
            val intentStart: PendingIntent = Utils.pendingServiceIntent(context, start)
            actions.add(Action.Builder(iconStart, titleStart, intentStart).build())

            content.setTextViewText(R.id.project_name, projectName)
            content.setViewVisibility(R.id.project_name, VISIBLE)
        }
        val notification: Builder = Builder(
            context,
            TIMER_NOTIFICATION_CHANNEL_ID
        )
            .setLocalOnly(true)
            .setOngoing(isRunning)
            .setCustomContentView(content)
            .setContentIntent(pendingShowApp)
            .setAutoCancel(!isRunning)
            .setPriority(NotificationManagerCompat.IMPORTANCE_HIGH)
            .setSmallIcon(R.drawable.ic_logo)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())

        for (action in actions) {
            notification.addAction(action)
        }

        return notification.build()
    }

    companion object {
        /** 计时通知渠道 ID */
        private const val TIMER_NOTIFICATION_CHANNEL_ID = "TraclockNotification"
    }
}
