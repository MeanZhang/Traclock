package com.mean.traclock.timer

import android.Manifest
import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.SystemClock
import android.view.View
import android.widget.RemoteViews
import androidx.annotation.DrawableRes
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.mean.traclock.CommonRes
import com.mean.traclock.common.getString
import com.mean.traclock.notifications.R
import kotlinx.datetime.Clock.System
import kotlinx.datetime.Instant

class AndroidNotifier(
    private val context: Context,
) : Notifier {
    companion object {
        /** 计时通知渠道 ID */
        private const val TIMER_NOTIFICATION_CHANNEL_ID = "TraclockNotification"

        /** 计时器通知 ID */
        private const val TIMER_NOTIFICATION_ID = Int.MAX_VALUE - 1
    }

    private val notificationManager =
        NotificationManagerCompat.from(context)

    private fun buildChannel() {
        if (notificationManager.getNotificationChannel(TIMER_NOTIFICATION_CHANNEL_ID) == null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel =
                    NotificationChannel(
                        TIMER_NOTIFICATION_CHANNEL_ID,
                        context.getString(CommonRes.strings.default_label),
                        NotificationManager.IMPORTANCE_DEFAULT,
                    )
                notificationManager.createNotificationChannel(channel)
            }
        }
    }

    /**
     * 更新并返回与给定 `intent` 对应的 [PendingIntent]
     *
     * @param context 启动服务的上下文
     * @param intent 要启动的服务的 [Intent]
     *
     * @return 将启动服务的 [PendingIntent]
     */
    private fun pendingServiceIntent(
        context: Context,
        intent: Intent,
    ): PendingIntent {
        val flag =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            } else {
                PendingIntent.FLAG_IMMUTABLE
            }
        return PendingIntent.getService(context, 0, intent, flag)
    }

    fun build(
        projectName: String,
        isRunning: Boolean,
        startTime: Instant,
    ): Notification {
        /** 点击通知时打开应用的 Intent */
        val showApp: Intent =
            Intent(context, TimerService::class.java)
                .setAction(TimerService.ACTION_SHOW_APP)

        val pendingShowApp = pendingServiceIntent(context, showApp)

        val pname: String = context.packageName
        val base: Long =
            SystemClock.elapsedRealtime() - (System.now() - startTime).inWholeMilliseconds

        val content = RemoteViews(pname, R.layout.chronometer_notif_content)
        content.setChronometer(R.id.chronometer, base, null, isRunning)

        val actions: MutableList<NotificationCompat.Action> = ArrayList(2)

        @DrawableRes val icon: Int = CommonRes.images.ic_logo.drawableResId
        if (isRunning) {
            // 停止按钮
            val stop: Intent =
                Intent(context, TimerService::class.java)
                    .setAction(TimerService.ACTION_STOP_TIMER)

            val titleStop: CharSequence = context.getString(CommonRes.strings.stop)
            val intentStop: PendingIntent = pendingServiceIntent(context, stop)
            actions.add(NotificationCompat.Action.Builder(icon, titleStop, intentStop).build())
        } else {
            // 开始按钮
            val start: Intent =
                Intent(context, TimerService::class.java)
                    .setAction(TimerService.ACTION_START_TIMER)

            val titleStart: CharSequence = context.getString(CommonRes.strings.start)
            val intentStart: PendingIntent = pendingServiceIntent(context, start)
            actions.add(NotificationCompat.Action.Builder(icon, titleStart, intentStart).build())
        }
        content.setTextViewText(
            R.id.project_name,
            "$projectName·" + (
                if (isRunning) {
                    context.getString(CommonRes.strings.tracking)
                        .uppercase()
                } else {
                    context.getString(CommonRes.strings.stopped).uppercase()
                }
            ),
        )
        content.setViewVisibility(R.id.project_name, View.VISIBLE)
        val notification: NotificationCompat.Builder =
            NotificationCompat.Builder(
                context,
                TIMER_NOTIFICATION_CHANNEL_ID,
            )
                .setLocalOnly(true)
                .setOngoing(isRunning)
                .setCustomContentView(content)
                .setContentIntent(pendingShowApp)
                .setAutoCancel(!isRunning)
                .setPriority(NotificationManagerCompat.IMPORTANCE_HIGH)
                .setSmallIcon(icon)
                .setStyle(NotificationCompat.DecoratedCustomViewStyle())

        for (action in actions) {
            notification.addAction(action)
        }

        return notification.build()
    }

    @SuppressLint("MissingPermission")
    override fun notify(
        projectName: String,
        isRunning: Boolean,
        startTime: Instant,
    ) {
        val notification: Notification =
            build(
                projectName,
                isRunning,
                startTime,
            )
        buildChannel()
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS,
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            notificationManager.notify(TIMER_NOTIFICATION_ID, notification)
        }
    }
}
