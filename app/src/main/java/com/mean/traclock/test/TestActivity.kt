package com.mean.traclock.test

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.view.WindowCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.mean.traclock.App
import com.mean.traclock.R
import com.mean.traclock.ui.theme.TraclockTheme
import kotlinx.coroutines.*

class TestActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val builder = NotificationCompat.Builder(
            App.context, App.context.getString(
                R.string.notice_channel_id
            )
        ).setSmallIcon(R.drawable.ic_logo)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentTitle("测试")
            .setOnlyAlertOnce(true)
            .setOngoing(true)
        val manager = NotificationManagerCompat.from(this)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            val scope = rememberCoroutineScope()
            var isTiming = false
            var time = 0
            val systemUiController = rememberSystemUiController()
            systemUiController.setSystemBarsColor(Color.Transparent)
            systemUiController.systemBarsDarkContentEnabled = !isSystemInDarkTheme()
            TraclockTheme {
                val scrollBehavior = remember { TopAppBarDefaults.pinnedScrollBehavior() }
                Scaffold(
                    modifier = Modifier
                        .systemBarsPadding()
                        .nestedScroll(scrollBehavior.nestedScrollConnection),
                    topBar = {
                        SmallTopAppBar(
                            title = { Text("Test") },
                            scrollBehavior = scrollBehavior
                        )
                    }
                ) { contentPadding ->
                    Column {
                        Button(onClick = {
                            isTiming = true
                            scope.launch {
                                while (isTiming) {
                                    builder.setContentText(time++.toString())
                                    manager.notify(1, builder.build())
                                    delay(1000)
                                }
                                cancel()
                            }
                        }) {
                            Text("开始")
                        }
                        Button(onClick = {
                            isTiming = false
                            builder.setOngoing(false)
                            manager.notify(1, builder.build())
                        }) {
                            Text("停止")
                        }
                        Test(contentPadding = contentPadding)
                    }
                }
            }
        }
    }
}

@Composable
fun Test(contentPadding: PaddingValues) {
    LazyColumn(
        contentPadding = contentPadding,
        modifier = Modifier.fillMaxSize(),
    ) {
        item {
            Text(text = "displayLarge", style = MaterialTheme.typography.displayLarge)
        }
        item {
            Text(text = "displayMedium", style = MaterialTheme.typography.displayMedium)
        }
        item {
            Text(text = "displaySmall", style = MaterialTheme.typography.displaySmall)
        }
        item {
            Text(text = "headlineLarge", style = MaterialTheme.typography.headlineLarge)
        }
        item {
            Text(text = "headlineMedium", style = MaterialTheme.typography.headlineMedium)
        }
        item {
            Text(text = "headlineSmall", style = MaterialTheme.typography.headlineSmall)
        }
        item {
            Text(text = "titleLarge", style = MaterialTheme.typography.titleLarge)
        }
        item {
            Text(text = "titleMedium", style = MaterialTheme.typography.titleMedium)
        }
        item {
            Text(text = "titleSmall", style = MaterialTheme.typography.titleSmall)
        }
        item {
            Text(text = "bodyLarge", style = MaterialTheme.typography.bodyLarge)
        }
        item {
            Text(text = "bodyMedium", style = MaterialTheme.typography.bodyMedium)
        }
        item {
            Text(text = "bodySmall", style = MaterialTheme.typography.bodySmall)
        }
        item {
            Text(text = "labelLarge", style = MaterialTheme.typography.labelLarge)
        }
        item {
            Text(text = "labelMediu", style = MaterialTheme.typography.labelMedium)
        }
        item {
            Text(text = "labelSmall", style = MaterialTheme.typography.labelSmall)
        }
        items(20) {
            Text(text = it.toString(), Modifier.padding(vertical = 16.dp))
        }
    }
}
