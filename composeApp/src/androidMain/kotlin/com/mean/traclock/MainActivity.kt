package com.mean.traclock

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.mean.traclock.ui.TraclockApp
import com.mean.traclock.ui.theme.TraclockTheme
import io.github.vinceglb.filekit.core.FileKit

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // FIXME 小米手浅色模式下导航栏不透明
        if (Build.MANUFACTURER == "Xiaomi" && Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            window.isNavigationBarContrastEnforced = false
        }
        FileKit.init(this)
        setContent {
            TraclockTheme {
                TraclockApp(
                    recordsRepo = App.recordsRepo,
                    recordWithProjectRepo = App.recordWithProjectRepo,
                    projectsRepo = App.projectsRepo,
                    timerRepo = App.timerRepo,
                )
            }
        }
    }
}
