package com.mean.traclock.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/** 广播接收器，用于处理通知按钮点击操作 */
class NotificationBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val isTiming = intent.getBooleanExtra("isTiming", true)
        if (isTiming) {
            intent.getStringExtra("projectName")?.let { TimingControl.startRecord(it) }
        } else {
            TimingControl.stopRecord()
        }
    }
}
