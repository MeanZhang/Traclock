package com.mean.traclock.ui.screens.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ViewDay
import androidx.compose.material.icons.filled.ViewHeadline
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.mean.traclock.ui.components.DateTitle
import com.mean.traclock.ui.components.NoData
import com.mean.traclock.ui.components.ProjectDurationItem
import com.mean.traclock.ui.components.RecordItem
import com.mean.traclock.ui.components.TimingCard
import com.mean.traclock.ui.navigation.HomeRoute
import com.mean.traclock.utils.PlatformUtils
import com.mean.traclock.utils.TimeUtils
import com.mean.traclock.viewmodels.MainViewModel
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import traclock.composeapp.generated.resources.Res
import traclock.composeapp.generated.resources.change_view
import traclock.composeapp.generated.resources.is_running_description
import traclock.composeapp.generated.resources.no_record
import ui.components.HomeScaffold
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TimeLine(
    viewModel: MainViewModel,
    navToProject: (Long) -> Unit,
    navToEditRecord: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    val isTiming by viewModel.isTiming.collectAsState(false)
    val timingProjectName by viewModel.timingProjectName.collectAsState(null)
    val detailView by viewModel.detailView.collectAsState()
    val recordsWithProject by viewModel.daysRecordsWithProject.collectAsState(mapOf())
    val daysProjectsDuration by viewModel.daysProjectsDuration.collectAsState(mapOf())
    val timeOfDays by viewModel.timeOfDays.collectAsState(mapOf())
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    HomeScaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        route = HomeRoute.TIMELINE,
        actions = {
            IconButton(onClick = viewModel::changeDetailView) {
                Icon(
                    imageVector = if (detailView) Icons.Default.ViewHeadline else Icons.Default.ViewDay,
                    contentDescription = stringResource(Res.string.change_view),
                )
            }
        },
        modifier = modifier,
    ) { contentPadding ->
        val listState = rememberLazyListState()
        if (recordsWithProject.isNotEmpty() || isTiming) {
            LazyColumn(
                modifier = modifier.padding(contentPadding),
                state = listState,
            ) {
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
                if (detailView) {
                    recordsWithProject.forEach { (date, data) ->
                        stickyHeader(key = date) {
                            DateTitle(
                                date = TimeUtils.getDateString(date),
                                duration = timeOfDays[date]?.toDuration(DurationUnit.MILLISECONDS) ?: Duration.ZERO,
                            )
                        }
                        items(data, key = { it.record.id }) { recordWithProject ->
                            RecordItem(
                                record = recordWithProject.record,
                                color = recordWithProject.project.color,
                                listState = listState,
                                navToEditRecord = navToEditRecord,
                                deleteRecord = viewModel::deleteRecord,
                                projectName = recordWithProject.project.name,
                                startTiming = {
                                    if (isTiming && !PlatformUtils.isAndroid) {
                                        scope.launch {
                                            snackbarHostState.showSnackbar(getString(Res.string.is_running_description))
                                        }
                                    } else {
                                        viewModel.startTiming(it)
                                    }
                                },
                            )
                        }
                    }
                } else {
                    daysProjectsDuration.forEach { (date, data) ->
                        stickyHeader(key = date) {
                            DateTitle(
                                date = TimeUtils.getDateString(date),
                                duration = timeOfDays[date]?.toDuration(DurationUnit.MILLISECONDS) ?: Duration.ZERO,
                            )
                        }
                        items(
                            data,
                            key = { it.let { projectDuration -> date.toString() + projectDuration.project.id } },
                        ) { projectDuration ->
                            ProjectDurationItem(
                                projectDuration = projectDuration,
                                navToProject = navToProject,
                                projectName = projectDuration.project.name,
                                color = projectDuration.project.color,
                                startTiming = {
                                    if (isTiming && !PlatformUtils.isAndroid) {
                                        scope.launch {
                                            snackbarHostState.showSnackbar(getString(Res.string.is_running_description))
                                        }
                                    } else {
                                        viewModel.startTiming(projectDuration.project.id)
                                    }
                                },
                            )
                        }
                    }
                }
            }
        } else {
            NoData(stringResource(Res.string.no_record), modifier = modifier.padding(contentPadding))
        }
    }
}
