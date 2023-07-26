package com.mean.traclock.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.mean.traclock.R
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
import com.mean.traclock.viewmodels.EditProjectViewModel
import com.mean.traclock.viewmodels.EditRecordViewModel
import com.mean.traclock.viewmodels.MainViewModel
import com.mean.traclock.viewmodels.ProjectViewModel

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun TraclockApp() {
    val navController = rememberAnimatedNavController()
    val state = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(state)
    val viewModel: MainViewModel = viewModel()
    Scaffold(
        topBar = {
            HomeTopBar(
                viewModel,
                navController,
                scrollBehavior,
                navToNewProject = { navController.navigate("${Route.EDIT_PROJECT}/0/true") },
            )
        },
        bottomBar = { HomeBottomBar(navController) },
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
    ) { contentPadding ->
        AnimatedNavHost(navController, Route.HOME) {
            navigation(
                startDestination = HomeRoute.TIMELINE.route,
                route = Route.HOME,
            ) {
                composable(HomeRoute.TIMELINE.route) {
                    TimeLine(
                        navToProject = { navController.navigate("${Route.PROJECT}/$it") },
                        navToEditRecord = { navController.navigate("${Route.EDIT_RECORD}/$it") },
                        contentPadding = contentPadding,
                    )
                }
                composable(HomeRoute.PROJECTS.route) {
                    Projects(
                        navToProject = { navController.navigate("${Route.PROJECT}/$it") },
                        navToEditRecord = { navController.navigate("${Route.EDIT_RECORD}/$it") },
                        contentPadding = contentPadding,
                    )
                }
                composable(HomeRoute.STATISTICS.route) {
                    Statistics(contentPadding)
                }
                composable(HomeRoute.SETTINGS.route) {
                    Settings(
                        navToBackupRestore = { navController.navigate(Route.BACKUP_RESTORE) },
                        navToFeddback = { navController.navigate(Route.FEEDBACK) },
                        navToAbout = { navController.navigate(Route.ABOUT) },
                        contentPadding = contentPadding,
                    )
                }
            }
            composable(
                "${Route.PROJECT}/{id}",
                arguments = listOf(
                    navArgument("id") { type = NavType.IntType },
                ),
            ) {
                Project(
                    navToProject = { navController.navigate("${Route.PROJECT}/$it") },
                    navBack = { navController.navigateUp() },
                    viewModel = hiltViewModel<ProjectViewModel>(),
                    navToEditProject = { id, isNew -> navController.navigate("${Route.EDIT_PROJECT}/$id/$isNew") },
                    navToEditRecord = { navController.navigate("${Route.EDIT_RECORD}/$it") },
                )
            }
            composable(
                "${Route.EDIT_PROJECT}/{id}/{isNew}",
                arguments = listOf(
                    navArgument("id") { type = NavType.IntType },
                    navArgument("isNew") { type = NavType.BoolType },
                ),
            ) {
                EditProject(
                    navBack = { navController.navigateUp() },
                    viewModel = hiltViewModel<EditProjectViewModel>(),
                )
            }
            composable(
                "${Route.EDIT_RECORD}/{id}",
                arguments = listOf(
                    navArgument("id") { type = NavType.LongType },
                ),
            ) {
                EditRecord(
                    viewModel = hiltViewModel<EditRecordViewModel>(),
                    navBack = { navController.navigateUp() },
                )
            }
            composable(Route.BACKUP_RESTORE) {
                BackupRestore(navBack = { navController.navigateUp() })
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
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeTopBar(
    viewModel: MainViewModel,
    navController: NavHostController,
    scrollBehavior: TopAppBarScrollBehavior,
    navToNewProject: () -> Unit,
) {
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination
    val title = when (currentDestination?.route) {
        HomeRoute.TIMELINE.route -> stringResource(id = R.string.timeline)
        HomeRoute.PROJECTS.route -> stringResource(id = R.string.projects)
        HomeRoute.STATISTICS.route -> stringResource(id = R.string.statistics)
        HomeRoute.SETTINGS.route -> stringResource(id = R.string.settings)
        else -> stringResource(id = R.string.app_name)
    }
    if (HomeRoute.values().map { it.route }.contains(currentDestination?.route)) {
        TopAppBar(
            title = { Text(title) },
            scrollBehavior = scrollBehavior,
            actions = {
                when (currentDestination?.route) {
                    HomeRoute.TIMELINE.route -> {
                        IconButton(onClick = { viewModel.changeDetailView() }) {
                            Icon(Icons.Default.SwapHoriz, stringResource(R.string.change_view))
                        }
                    }

                    HomeRoute.PROJECTS.route -> {
                        IconButton(onClick = navToNewProject) {
                            Icon(Icons.Default.Add, stringResource(R.string.new_project))
                        }
                    }
                }
            },
        )
    }
}

@Composable
private fun HomeBottomBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    if (HomeRoute.values().map { it.route }.contains(currentDestination?.route)) {
        NavigationBar {
            HomeRoute.values().forEach { destination ->
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
                    alwaysShowLabel = false,
                )
            }
        }
    }
}
