package com.mean.traclock.ui.screens.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mean.traclock.R
import com.mean.traclock.data.DataModel
import com.mean.traclock.database.Project
import com.mean.traclock.database.Record
import com.mean.traclock.ui.components.DateTitle
import com.mean.traclock.ui.components.NoData
import com.mean.traclock.ui.components.RecordItem
import com.mean.traclock.ui.components.TimingCard
import com.mean.traclock.utils.TimeUtils
import com.mean.traclock.viewmodels.MainViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun TimeLine(
    viewModel: MainViewModel = viewModel(),
    navToProject: (Int) -> Unit,
    navToEditRecord: (Long) -> Unit,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    val detailView by viewModel.detailView.collectAsState()
    val recordsFlow = if (detailView) viewModel.records else viewModel.projectsTimeOfDays
    Content(
        recordsFlow,
        viewModel.timeOfDays,
        detailView,
        DataModel.dataModel.isRunning,
        DataModel.dataModel.projectId,
        DataModel.dataModel.startTime,
        DataModel.dataModel.projects,
        contentPadding,
        navToProject = navToProject,
        navToEditRecord = navToEditRecord,
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun Content(
    recordsFlow: Flow<Map<Int, List<Record>>>,
    timeFlow: Flow<Map<Int, Long>>,
    detailView: Boolean,
    isTimingFlow: StateFlow<Boolean>,
    timingProjectFlow: StateFlow<Int?>,
    startTimeFlow: StateFlow<Long>,
    projects: Map<Int, Project>,
    contentPadding: PaddingValues,
    navToProject: (Int) -> Unit,
    navToEditRecord: (Long) -> Unit,
) {
    val isTiming by isTimingFlow.collectAsState(false)
    val records by recordsFlow.collectAsState(mapOf())
    val time by timeFlow.collectAsState(mapOf())

    val listState = rememberLazyListState()
    val timingProject by timingProjectFlow.collectAsState()
    val startTime by startTimeFlow.collectAsState()
    LazyColumn(
        Modifier.padding(contentPadding),
        state = listState,
    ) {
        item {
            TimingCard(
                timingProject,
                startTime,
                isTiming,
            )
        }
        if (records.isNotEmpty()) {
            records.forEach { (date, data) ->
                stickyHeader {
                    DateTitle(
                        date = TimeUtils.getDataString(date),
                        duration = time[date] ?: 0L,
                    )
                }
                items(data) { record ->
                    RecordItem(
                        record = record,
                        color = Color(projects[record.project]?.color ?: 0),
                        detailView = detailView,
                        listState = listState,
                        navToProject = navToProject,
                        navToEditRecord = navToEditRecord,
                    )
                }
            }
        } else {
            item {
                NoData(stringResource(R.string.no_record))
            }
        }
    }
}
