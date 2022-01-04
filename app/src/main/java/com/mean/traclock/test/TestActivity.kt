package com.mean.traclock.test

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.CardTravel
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.HolidayVillage
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.mean.traclock.ui.theme.TraclockTheme

class TestActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            val systemUiController = rememberSystemUiController()
            systemUiController.setSystemBarsColor(Color.Transparent)
            systemUiController.systemBarsDarkContentEnabled = !isSystemInDarkTheme()
            TraclockTheme {
                ProvideWindowInsets {
                    val scrollBehavior = remember { TopAppBarDefaults.pinnedScrollBehavior() }
                    Scaffold(
                        modifier = Modifier
                            .padding(rememberInsetsPaddingValues(LocalWindowInsets.current.systemBars))
                            .nestedScroll(scrollBehavior.nestedScrollConnection),
                        topBar = {
                            SmallTopAppBar(
                                title = { Text("Test") },
                                scrollBehavior = scrollBehavior
                            )
                        },
                        bottomBar = {
                            var selected by remember { mutableStateOf(0) }

                            NavigationBar {
                                NavigationBarItem(
                                    selected = selected == 0,
                                    onClick = { selected = 0 },
                                    icon = {
                                        Icon(
                                            Icons.Default.Fastfood,
                                            contentDescription = null
                                        )
                                    },
                                    label = { Text(text = "1") },
                                    alwaysShowLabel = false
                                )
                                NavigationBarItem(
                                    selected = selected == 1,
                                    onClick = { selected = 1 },
                                    icon = {
                                        Icon(
                                            Icons.Default.CardTravel,
                                            contentDescription = null
                                        )
                                    },
                                    label = { Text(text = "2") },
                                    alwaysShowLabel = false
                                )
                                NavigationBarItem(
                                    selected = selected == 2,
                                    onClick = { selected = 2 },
                                    icon = {
                                        Icon(
                                            Icons.Default.HolidayVillage,
                                            contentDescription = null
                                        )
                                    },
                                    label = { Text(text = "3") },
                                    alwaysShowLabel = false
                                )
                                NavigationBarItem(
                                    selected = selected == 3,
                                    onClick = { selected = 3 },
                                    icon = { Icon(Icons.Default.Alarm, contentDescription = null) },
                                    label = { Text(text = "4") },
                                    alwaysShowLabel = false
                                )
                            }
                        }
                    ) { contentPadding ->
                        Box {
                            Test(contentPadding = contentPadding)
                        }
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

@Composable
@Preview(showBackground = true)
fun Preview() {
    Test(PaddingValues(16.dp))
}