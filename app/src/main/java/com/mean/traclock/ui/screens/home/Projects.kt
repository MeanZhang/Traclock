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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.mean.traclock.ui.components.HomeScaffold
import com.mean.traclock.ui.components.RecordItem
import com.mean.traclock.ui.components.TimingCard
import com.mean.traclock.ui.navigation.HomeRoute
import com.mean.traclock.viewmodels.MainViewModel

@Composable
fun Projects(
    viewModel: MainViewModel,
    navToProject: (Long) -> Unit,
    navToNewProject: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val isTiming by viewModel.isTiming.collectAsState(false)
    val projectsTime by viewModel.projectsTime.collectAsState(listOf())
    HomeScaffold(
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
                        projectName = viewModel.projects[viewModel.timingProjectId]?.name ?: "",
                        startTime = viewModel.startTime,
                        stopTiming = viewModel::stopTiming,
                    )
                }
            }
            items(projectsTime, key = { it.project }) { record ->
                RecordItem(
                    projectName = viewModel.projects[record.project]?.name ?: "",
                    record = record,
                    color = Color(viewModel.projects[record.project]?.color ?: 0),
                    detailView = false,
                    navToProject = navToProject,
                    navToEditRecord = {},
                    deleteRecord = viewModel::deleteRecord,
                    startTiming = { viewModel.startTiming(record.project) },
                )
            }
        }
    }
}
