package com.mean.traclock

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.core.view.WindowCompat
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.mean.traclock.ui.components.BottomBar
import com.mean.traclock.ui.screens.Projects
import com.mean.traclock.ui.screens.Settings
import com.mean.traclock.ui.screens.Statistics
import com.mean.traclock.ui.screens.TimeLine
import com.mean.traclock.ui.theme.TraclockTheme
import com.mean.traclock.ui.utils.Destinations
import com.mean.traclock.viewmodels.MainViewModel
import kotlinx.coroutines.DelicateCoroutinesApi

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    @OptIn(
        DelicateCoroutinesApi::class,
        ExperimentalAnimationApi::class,
        ExperimentalMaterial3Api::class
    )
    @SuppressLint("UnrememberedMutableState")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            TraclockTheme {
                val navController = rememberAnimatedNavController()
                Scaffold(bottomBar = { HomeBottomBar(navController) }) { contentPadding ->
                    AnimatedNavHost(navController, Destinations.TIMELINE.route) {
                        composable(Destinations.TIMELINE.route) {
                            TimeLine(
                                this@MainActivity,
                                viewModel,
                                contentPadding
                            )
                        }
                        composable(Destinations.PROJECTS.route) {
                            Projects(
                                this@MainActivity,
                                viewModel,
                                contentPadding
                            )
                        }
                        composable(Destinations.STATISTICS.route) {
                            Statistics(contentPadding)
                        }
                        composable(Destinations.SETTINGS.route) {
                            Settings(this@MainActivity, contentPadding)
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun HomeBottomBar(navController: NavHostController) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        BottomBar {
            Destinations.values().forEach { destination ->
                NavigationBarItem(
                    selected = currentDestination?.hierarchy?.any { it.route == destination.route } == true,
                    onClick = {
                        navController.navigate(destination.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = { Icon(destination.icon, stringResource(destination.titleId)) },
                    label = { Text(stringResource(destination.titleId)) },
                    alwaysShowLabel = false
                )
            }
        }
    }
}
