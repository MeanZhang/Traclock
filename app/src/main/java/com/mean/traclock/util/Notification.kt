package com.mean.traclock.util

import android.app.Notification
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.mean.traclock.TraclockApplication
import com.mean.traclock.TraclockApplication.Companion.context
import com.mean.traclock.R
import kotlinx.coroutines.*

@DelicateCoroutinesApi
fun startNotify(projectName: String, startTime: Long) {
    GlobalScope.launch {
        while (TraclockApplication.isTiming.value==true) {
            notify(projectName, startTime, true)
            delay(1000)
        }
        cancel()
    }
}

fun stopNotify(projectName: String, startTime: Long) {
    notify(projectName, startTime, false)
}

private fun notify(projectName: String, startTime: Long, isTiming: Boolean) {
//        val intent = Intent(this, TestActivity::class.java)
//        val pendingIntent: PendingIntent =
//            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
    val time = getDurationString(startTime, System.currentTimeMillis())
    val builder = NotificationCompat.Builder(
        context, context.getString(
            R.string.notice_channel_id
        )
    )
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle(projectName)
        .setContentText(time)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//            .setContentIntent(pendingIntent)
        .setAutoCancel(true)
        .setOnlyAlertOnce(true)
    with(NotificationManagerCompat.from(context)) {
        notify(1, builder.build().apply {
            if (isTiming)
                flags = Notification.FLAG_ONGOING_EVENT
        })
    }
}