package com.mean.traclock

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowCompat
import com.mean.traclock.ui.TraclockApp
import com.mean.traclock.ui.theme.TraclockTheme
import io.github.vinceglb.filekit.core.FileKit

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // FIXME 小米手浅色模式下导航栏不透明
        if (Build.MANUFACTURER == "Xiaomi") {
            WindowCompat.setDecorFitsSystemWindows(window, false)
        } else {
            enableEdgeToEdge()
        }
        FileKit.init(this)
        setContent {
            TraclockTheme {
                TraclockApp(App.recordsRepo, App.projectsRepo, App.timerRepo)
            }
        }
    }
}
