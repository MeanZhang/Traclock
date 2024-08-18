package com.mean.traclock

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.mean.traclock.ui.TraclockApp
import com.mean.traclock.ui.theme.TraclockTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            TraclockTheme {
                TraclockApp(App.recordsRepo, App.projectsRepo, App.timerRepo)
            }
        }
    }
}
