package com.mean.traclock.data.repository

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.SystemClock
import android.view.View.VISIBLE
import android.widget.RemoteViews
import androidx.annotation.DrawableRes
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.Action
import androidx.core.app.NotificationCompat.Builder
import androidx.core.app.NotificationManagerCompat
import com.mean.traclock.R
import com.mean.traclock.timer.TimerService
import com.mean.traclock.utils.TimeUtils
import com.mean.traclock.utils.Utils

actual class NotificationRepository(
    private val context: Context,
) {
    companion object {
        /** 计时通知渠道 ID */
        private const val TIMER_NOTIFICATION_CHANNEL_ID = "TraclockNotification"

        /** 计时器通知 ID */
        private const val TIMER_NOTIFICATION_ID = Int.MAX_VALUE - 1
    }

    private var channelBuilt = false

    private val notificationManager =
        NotificationManagerCompat.from(context)

    private fun buildChannel() {
        val channel =
            NotificationChannel(
                TIMER_NOTIFICATION_CHANNEL_ID,
                context.getString(R.string.default_label),
                NotificationManager.IMPORTANCE_DEFAULT,
            )
        notificationManager.createNotificationChannel(channel)
        channelBuilt = true
    }

    fun build(
        projectName: String,
        isRunning: Boolean,
        startTime: Long,
    ): Notification {
        /** 点击通知时打开应用的 Intent */
        val showApp: Intent =
            Intent(context, TimerService::class.java)
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
            val stop: Intent =
                Intent(context, TimerService::class.java)
                    .setAction(TimerService.ACTION_STOP_TIMER)

            @DrawableRes val iconStop: Int = R.drawable.ic_logo
            val titleStop: CharSequence = res.getText(R.string.stop)
            val intentStop: PendingIntent = Utils.pendingServiceIntent(context, stop)
            actions.add(Action.Builder(iconStop, titleStop, intentStop).build())
        } else {
            // 开始按钮
            val start: Intent =
                Intent(context, TimerService::class.java)
                    .setAction(TimerService.ACTION_START_TIMER)

            @DrawableRes val iconStart: Int = R.drawable.ic_logo
            val titleStart: CharSequence = res.getText(R.string.start)
            val intentStart: PendingIntent = Utils.pendingServiceIntent(context, start)
            actions.add(Action.Builder(iconStart, titleStart, intentStart).build())
        }
        content.setTextViewText(
            R.id.project_name,
            "$projectName·" + (
                if (isRunning) {
                    context.getString(R.string.tracking)
                        .uppercase()
                } else {
                    context.getString(R.string.stopped).uppercase()
                }
            ),
        )
        content.setViewVisibility(R.id.project_name, VISIBLE)
        val notification: Builder =
            Builder(
                context,
                TIMER_NOTIFICATION_CHANNEL_ID,
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

    actual fun notify(
        projectName: String,
        isRunning: Boolean,
        startTime: Long,
    ) {
        val notification: Notification =
            build(
                projectName,
                isRunning,
                startTime,
            )
        if (!channelBuilt) {
            buildChannel()
        }
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS,
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        notificationManager.notify(TIMER_NOTIFICATION_ID, notification)
    }
}
