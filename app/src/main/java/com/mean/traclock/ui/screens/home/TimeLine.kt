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
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.mean.traclock.R
import com.mean.traclock.ui.components.DateTitle
import com.mean.traclock.ui.components.HomeScaffold
import com.mean.traclock.ui.components.NoData
import com.mean.traclock.ui.components.RecordItem
import com.mean.traclock.ui.components.TimingCard
import com.mean.traclock.ui.navigation.HomeRoute
import com.mean.traclock.utils.TimeUtils
import com.mean.traclock.viewmodels.MainViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TimeLine(
    viewModel: MainViewModel,
    navToProject: (Long) -> Unit,
    navToEditRecord: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    val isTiming by viewModel.isTiming.collectAsState(false)
    val detailView by viewModel.detailView.collectAsState()
    val records by viewModel.records.collectAsState(mapOf())
    val projectsTimeOfDays by viewModel.projectsTimeOfDays.collectAsState(mapOf())
    val timeOfDays by viewModel.timeOfDays.collectAsState(mapOf())
    val data = if (detailView) records else projectsTimeOfDays
    HomeScaffold(
        route = HomeRoute.TIMELINE,
        actions = {
            IconButton(onClick = viewModel::changeDetailView) {
                Icon(Icons.Default.SwapHoriz, stringResource(R.string.change_view))
            }
        },
        modifier = modifier,
    ) { contentPadding ->
        val listState = rememberLazyListState()
        if (data.isNotEmpty() || isTiming) {
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
                            projectName = viewModel.projects[viewModel.timingProjectId]?.name ?: "",
                            startTime = viewModel.startTime,
                            stopTiming = viewModel::stopTiming,
                        )
                    }
                }
                data.forEach { (date, data) ->
                    stickyHeader(key = date) {
                        DateTitle(
                            date = TimeUtils.getDataString(date),
                            duration = timeOfDays[date] ?: 0L,
                        )
                    }
                    items(data, key = { it.id }) { record ->
                        RecordItem(
                            record = record,
                            color = Color(viewModel.projects[record.project]?.color ?: 0),
                            detailView = detailView,
                            listState = listState,
                            navToProject = navToProject,
                            navToEditRecord = navToEditRecord,
                            deleteRecord = viewModel::deleteRecord,
                            projectName = viewModel.projects[record.project]?.name ?: "",
                            startTiming = viewModel::startTiming,
                        )
                    }
                }
            }
        } else {
            NoData(stringResource(R.string.no_record), modifier = modifier.padding(contentPadding))
        }
    }
}
