package com.mean.traclock.ui.screens.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.mean.traclock.ui.HomeRoute
import com.mean.traclock.ui.components.HomeScaffold
import com.mean.traclock.ui.components.ProjectDurationItem
import com.mean.traclock.ui.components.TimingCard
import com.mean.traclock.viewmodels.MainViewModel

@Composable
fun Projects(
    viewModel: MainViewModel,
    navToProject: (Long) -> Unit,
    navToNewProject: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val isTiming by viewModel.isTiming.collectAsState(false)
    val timingProjectName by viewModel.timingProjectName.collectAsState(null)
    val projectsTime by viewModel.projectsTime.collectAsState(listOf())
    val snackbarHostState = remember { SnackbarHostState() }
    HomeScaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        route = HomeRoute.PROJECTS,
        actions = {
            IconButton(onClick = navToNewProject) {
                Icon(Icons.Default.Add, null)
            }
        },
    ) { contentPadding ->
        LazyColumn(contentPadding = contentPadding, modifier = modifier) {
            item {
                AnimatedVisibility(
                    visible = isTiming,
                    enter = expandVertically(),
                    exit = shrinkVertically(),
                ) {
                    TimingCard(
                        projectName = timingProjectName ?: "",
                        startTime = viewModel.startTime,
                        stopTiming = viewModel::stopTiming,
                    )
                }
            }
            items(projectsTime, key = { it.project.id }) { projectDuration ->
                ProjectDurationItem(
                    projectDuration = projectDuration,
                    navToProject = navToProject,
                    projectName = projectDuration.project.name,
                    color = projectDuration.project.color,
                    startTiming = { viewModel.startTiming(projectDuration.project.id) },
                )
            }
        }
    }
}
