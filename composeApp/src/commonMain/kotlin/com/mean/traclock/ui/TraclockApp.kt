package com.mean.traclock.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.mean.traclock.data.repository.ProjectsRepository
import com.mean.traclock.data.repository.RecordWithProjectRepository
import com.mean.traclock.data.repository.RecordsRepository
import com.mean.traclock.data.repository.TimerRepository
import com.mean.traclock.ui.components.HomeBottomBar
import com.mean.traclock.ui.navigation.HomeRoute
import com.mean.traclock.ui.navigation.Route
import com.mean.traclock.ui.screens.EditProject
import com.mean.traclock.ui.screens.EditRecord
import com.mean.traclock.ui.screens.Project
import com.mean.traclock.ui.screens.home.Projects
import com.mean.traclock.ui.screens.home.Settings
import com.mean.traclock.ui.screens.home.Statistics
import com.mean.traclock.ui.screens.home.TimeLine
import com.mean.traclock.ui.screens.settings.About
import com.mean.traclock.ui.screens.settings.BackupRestore
import com.mean.traclock.ui.screens.settings.Feedback
import com.mean.traclock.ui.screens.settings.OpenSourceLicenses
import com.mean.traclock.ui.utils.ApplyForNotificationPermission
import com.mean.traclock.viewmodels.BackupRestoreViewModel
import com.mean.traclock.viewmodels.EditProjectViewModel
import com.mean.traclock.viewmodels.EditRecordViewModel
import com.mean.traclock.viewmodels.MainViewModel
import com.mean.traclock.viewmodels.ProjectViewModel
import com.mean.traclock.viewmodels.StatisticViewModel

@Composable
fun TraclockApp(
    recordsRepo: RecordsRepository,
    recordWithProjectRepo: RecordWithProjectRepository,
    projectsRepo: ProjectsRepository,
    timerRepo: TimerRepository,
) {
    ApplyForNotificationPermission()
    val viewModel =
        viewModel {
            MainViewModel(
                recordsRepo = recordsRepo,
                recordWithProjectRepo = recordWithProjectRepo,
                timerRepo = timerRepo,
            )
        }
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination?.route ?: HomeRoute.TIMELINE.route
    Column(modifier = Modifier.fillMaxSize()) {
        NavHost(navController, Route.HOME, modifier = Modifier.weight(1f)) {
            navigation(
                startDestination = HomeRoute.TIMELINE.route,
                route = Route.HOME,
            ) {
                composable(HomeRoute.TIMELINE.route) {
                    TimeLine(
                        viewModel = viewModel,
                        navToProject = { navController.navigate("${Route.PROJECT}/$it") },
                        navToEditRecord = { navController.navigate("${Route.EDIT_RECORD}/$it") },
                    )
                }
                composable(HomeRoute.PROJECTS.route) {
                    Projects(
                        viewModel = viewModel,
                        navToProject = { navController.navigate("${Route.PROJECT}/$it") },
                        navToNewProject = { navController.navigate(Route.EDIT_PROJECT) },
                        modifier = Modifier.fillMaxSize(),
                    )
                }
                composable(HomeRoute.STATISTICS.route) {
                    Statistics(
                        modifier = Modifier.fillMaxSize(),
                        viewModel =
                            viewModel {
                                StatisticViewModel(
                                    recordsRepo = recordsRepo,
                                    recordWithProjectRepo = recordWithProjectRepo,
                                    projectsRepo = projectsRepo,
                                    timerRepo = timerRepo,
                                )
                            },
                    )
                }
                composable(HomeRoute.SETTINGS.route) {
                    Settings(
                        navToBackupRestore = { navController.navigate(Route.BACKUP_RESTORE) },
                        navToFeddback = { navController.navigate(Route.FEEDBACK) },
                        navToAbout = { navController.navigate(Route.ABOUT) },
                        navTo = { navController.navigate(it.route) },
                        modifier = Modifier.fillMaxSize(),
                    )
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
                    viewModel =
                        viewModel {
                            ProjectViewModel(
                                savedStateHandle =
                                    SavedStateHandle(
                                        mapOf("id" to navController.currentBackStackEntry?.arguments?.getLong("id")),
                                    ),
                                projectsRepo = projectsRepo,
                                recordsRepo = recordsRepo,
                                timerRepo = timerRepo,
                            )
                        },
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
                    viewModel =
                        viewModel {
                            EditProjectViewModel(
                                projectsRepo = projectsRepo,
                                savedStateHandle =
                                    SavedStateHandle(
                                        mapOf("id" to navController.currentBackStackEntry?.arguments?.getString("id")),
                                    ),
                            )
                        },
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
                    viewModel =
                        viewModel {
                            EditRecordViewModel(
                                savedStateHandle =
                                    SavedStateHandle(
                                        mapOf("id" to navController.currentBackStackEntry?.arguments?.getLong("id")),
                                    ),
                                recordsRepo = recordsRepo,
                                recordWithProjectRepo = recordWithProjectRepo,
                                projectsRepo = projectsRepo,
                            )
                        },
                    navBack = { navController.navigateUp() },
                )
            }
            composable(Route.BACKUP_RESTORE) {
                BackupRestore(
                    viewModel = viewModel { BackupRestoreViewModel(projectsRepo, recordsRepo, recordWithProjectRepo) },
                    navBack = { navController.navigateUp() },
                )
            }
            composable(Route.FEEDBACK) {
                Feedback(navBack = { navController.navigateUp() })
            }
            composable(Route.ABOUT) {
                About(
                    navBack = { navController.navigateUp() },
                    navToOpenSourceLicenses = { navController.navigate(Route.OPEN_SOURCE_LICENSES) },
                )
            }
            composable(Route.OPEN_SOURCE_LICENSES) {
                OpenSourceLicenses(navBack = { navController.navigateUp() })
            }
        }

        HomeBottomBar(
            currentRoute = currentDestination,
            navTo = {
                navController.navigate(it.route) {
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
