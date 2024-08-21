package com.mean.traclock.data.repository

import androidx.compose.ui.window.Notification
import androidx.compose.ui.window.TrayState
import co.touchlab.kermit.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import traclock.composeapp.generated.resources.Res
import traclock.composeapp.generated.resources.app_name
import traclock.composeapp.generated.resources.stopped
import traclock.composeapp.generated.resources.tracking

actual class NotificationRepository {
    private lateinit var trayState: TrayState
    private var initialized = false

    fun init(trayState: TrayState) {
        this.trayState = trayState
        this.initialized = true
        Logger.d("NotificationRepository初始化成功")
    }

    actual fun notify(
        projectName: String,
        isRunning: Boolean,
        startTime: Long,
    ) {
        if (initialized) {
            CoroutineScope(Dispatchers.Default).launch {
                val text =
                    "$projectName·" + (
                        if (isRunning) {
                            getString(Res.string.tracking)
                        } else {
                            getString(Res.string.stopped)
                        }
                    )
                val notification = Notification(getString(Res.string.app_name), text)
                trayState.sendNotification(notification)
            }
        }
    }
}
