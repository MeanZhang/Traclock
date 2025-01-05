package com.mean.traclock.utils

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.mean.traclock.App

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual object Utils {
    actual fun openUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        App.context.startActivity(intent)
    }

    /**
     * 更新并返回与给定 `intent` 对应的 [PendingIntent]
     *
     * @param context 启动服务的上下文
     * @param intent 要启动的服务的 [Intent]
     *
     * @return 将启动服务的 [PendingIntent]
     */
    fun pendingServiceIntent(
        context: Context,
        intent: Intent,
    ): PendingIntent {
        val flag = PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        return PendingIntent.getService(context, 0, intent, flag)
    }
}
