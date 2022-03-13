package com.mean.traclock.test

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Timeline
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.mean.traclock.ui.components.BottomBar
import com.mean.traclock.ui.components.TopBar
import com.mean.traclock.ui.theme.TraclockTheme
import com.mean.traclock.ui.utils.SetSystemBar

class TestActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            TraclockTheme {
                SetSystemBar()
                val scrollBehavior = remember { TopAppBarDefaults.pinnedScrollBehavior() }
                Scaffold(
                    topBar = {
                        TopBar(
                            title = "Test",
                            scrollBehavior = scrollBehavior,
                        )
                    },
                    bottomBar = {
                        var selected by remember { mutableStateOf(1) }
                        BottomBar {
                            for (i in 1..4) {
                                NavigationBarItem(
                                    selected = selected == i,
                                    onClick = { selected = i },
                                    icon = { Icon(Icons.Outlined.Timeline, null) },
                                    label = { Text("测试") },
                                    alwaysShowLabel = false
                                )
                            }
                        }
                    },
                    modifier = Modifier
                        .nestedScroll(scrollBehavior.nestedScrollConnection),
                ) { contentPadding ->
                    Column {
                        Test(contentPadding = contentPadding)
                    }
                }
            }
        }
    }
}

@Composable
fun Test(contentPadding: PaddingValues = PaddingValues(0.dp)) {
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
