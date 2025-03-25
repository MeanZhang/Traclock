package com.mean.traclock.timer

import androidx.compose.ui.window.Notification
import androidx.compose.ui.window.TrayState
import com.mean.traclock.CommonRes
import com.mean.traclock.common.getString
import kotlinx.datetime.Instant

class DesktopNotifier : Notifier {
    private lateinit var trayState: TrayState

    fun init(trayState: TrayState) {
        this.trayState = trayState
    }

    override fun notify(
        projectName: String,
        isRunning: Boolean,
        startTime: Instant,
    ) {
        val text =
            "$projectName·" + (
                if (isRunning) {
                    getString(CommonRes.strings.tracking)
                } else {
                    getString(CommonRes.strings.stopped)
                }
            )
        val notification = Notification(getString(CommonRes.strings.app_name), text, type = Notification.Type.Info)
        trayState.sendNotification(notification)
    }
}
