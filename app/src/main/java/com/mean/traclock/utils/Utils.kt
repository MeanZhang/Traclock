package com.mean.traclock.utils

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Looper

fun Context.openURL(url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    startActivity(intent)
}

object Utils {

    fun enforceMainLooper() {
        if (Looper.getMainLooper() != Looper.myLooper()) {
            throw IllegalAccessError("May only call from main thread.")
        }
    }

    /**
     * @return 当前设备是否是 Android 8.0 或更高版本
     */
    val isOOrLater: Boolean
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O

    /**
     * 更新并返回与给定 `intent` 对应的 [PendingIntent]
     *
     * @param context 启动服务的上下文
     * @param intent 要启动的服务的 [Intent]
     *
     * @return 将启动服务的 [PendingIntent]
     */
    fun pendingServiceIntent(context: Context, intent: Intent): PendingIntent {
        val flag = if (isOOrLater) {
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }
        return PendingIntent.getService(context, 0, intent, flag)
    }
}
