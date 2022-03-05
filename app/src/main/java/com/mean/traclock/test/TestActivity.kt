package com.mean.traclock.test

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.mean.traclock.ui.theme.TraclockTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow

class TestActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            val a by test().collectAsState(0)

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
                        Text(a.toString())
                        Test(contentPadding = contentPadding)
                    }
                }
            }
        }
    }
}

fun test() = flow {
    for (i in 1..100) {
        delay(1000)
        emit(i)
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
