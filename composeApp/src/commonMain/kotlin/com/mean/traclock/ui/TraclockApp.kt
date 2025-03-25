package com.mean.traclock.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.mean.traclock.settings.ui.settingsScreen
import com.mean.traclock.statistic.Statistics
import com.mean.traclock.ui.components.HomeBottomBar
import com.mean.traclock.ui.navigation.Route
import com.mean.traclock.ui.screens.EditProject
import com.mean.traclock.ui.screens.EditRecord
import com.mean.traclock.ui.screens.Project
import com.mean.traclock.ui.screens.home.Projects
import com.mean.traclock.ui.screens.home.TimeLine
import com.mean.traclock.ui.utils.ApplyForNotificationPermission
import com.mean.traclock.viewmodels.MainViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun TraclockApp() {
    ApplyForNotificationPermission()
    val viewModel: MainViewModel = koinViewModel()
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination?.route ?: HomeRoute.TIMELINE.name
    Column(modifier = Modifier.fillMaxSize()) {
        NavHost(navController, Route.HOME, modifier = Modifier.weight(1f)) {
            navigation(
                startDestination = HomeRoute.TIMELINE.name,
                route = Route.HOME,
            ) {
                composable(HomeRoute.TIMELINE.name) {
                    TimeLine(
                        viewModel = viewModel,
                        navToProject = { navController.navigate("${Route.PROJECT}/$it") },
                        navToEditRecord = { navController.navigate("${Route.EDIT_RECORD}/$it") },
                    )
                }
                composable(HomeRoute.PROJECTS.name) {
                    Projects(
                        viewModel = viewModel,
                        navToProject = { navController.navigate("${Route.PROJECT}/$it") },
                        navToNewProject = { navController.navigate(Route.EDIT_PROJECT) },
                        modifier = Modifier.fillMaxSize(),
                    )
                }
                composable(HomeRoute.STATISTICS.name) {
                    Statistics(modifier = Modifier.fillMaxSize())
                }
            }
            composable(
                "${Route.PROJECT}/{id}",
                arguments =
                    listOf(
                        navArgument("id") { type = NavType.LongType },
                    ),
            ) {
                Project(
                    navToProject = { navController.navigate("${Route.PROJECT}/$it") },
                    navBack = { navController.navigateUp() },
                    navToEditProject = { id -> navController.navigate("${Route.EDIT_PROJECT}?id=$id") },
                    navToEditRecord = { navController.navigate("${Route.EDIT_RECORD}/$it") },
                )
            }
            composable(
                "${Route.EDIT_PROJECT}?id={id}",
                arguments =
                    listOf(
                        navArgument("id") {
                            type = NavType.StringType
                            nullable = true
                        },
                    ),
            ) {
                EditProject(
                    navBack = { navController.navigateUp() },
                )
            }
            composable(
                "${Route.EDIT_RECORD}/{id}",
                arguments =
                    listOf(
                        navArgument("id") { type = NavType.LongType },
                    ),
            ) {
                EditRecord(
                    navBack = { navController.navigateUp() },
                )
            }
            settingsScreen(navController)
        }

        HomeBottomBar(
            currentRoute = currentDestination,
            navTo = {
                navController.navigate(it.name) {
                    navController.graph.findStartDestination().route?.let { route ->
                        popUpTo(route) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            },
        )
    }
}
