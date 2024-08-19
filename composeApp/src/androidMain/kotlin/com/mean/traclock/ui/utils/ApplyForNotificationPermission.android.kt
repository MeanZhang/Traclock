package com.mean.traclock.ui.utils

import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import co.touchlab.kermit.Logger
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
actual fun ApplyForNotificationPermission() {
    val notificationPermissionState =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            rememberPermissionState(android.Manifest.permission.POST_NOTIFICATIONS)
        } else {
            null
        }
    LaunchedEffect(Unit) {
        if (notificationPermissionState == null) {
            Logger.i { "Android SDK低于33，无需申请权限" }
        } else {
            if (notificationPermissionState.status.isGranted) {
                Logger.i { "已获取通知权限，不再申请" }
            } else {
                notificationPermissionState.launchPermissionRequest()
                Logger.i { "申请通知权限" }
            }
        }
    }
}
