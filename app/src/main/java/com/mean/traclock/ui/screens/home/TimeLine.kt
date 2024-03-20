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
import com.mean.traclock.R
import com.mean.traclock.data.DataModel
import com.mean.traclock.database.Project
import com.mean.traclock.database.Record
import com.mean.traclock.ui.components.DateTitle
import com.mean.traclock.ui.components.NoData
import com.mean.traclock.ui.components.RecordItem
import com.mean.traclock.ui.components.TimingCard
import com.mean.traclock.utils.TimeUtils
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.coroutines.flow.StateFlow

@Composable
fun TimeLine(
    detailView: Boolean,
    records: Map<Int, List<Record>>,
    projectsTimeOfDays: Map<Int, List<Record>>,
    timeOfDays: Map<Int, Long>,
    navToProject: (Int) -> Unit,
    navToEditRecord: (Long) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    val data = if (detailView) records else projectsTimeOfDays

    Content(
        data,
        timeOfDays,
        detailView,
        DataModel.dataModel.isRunning,
        DataModel.dataModel.projectId,
        DataModel.dataModel.startTime,
        DataModel.dataModel.projects,
        contentPadding,
        navToProject = navToProject,
        navToEditRecord = navToEditRecord,
        modifier = modifier,
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun Content(
    records: Map<Int, List<Record>>,
    time: Map<Int, Long>,
    detailView: Boolean,
    isTimingFlow: StateFlow<Boolean>,
    timingProjectFlow: StateFlow<Int?>,
    startTimeFlow: StateFlow<Long>,
    projects: ImmutableMap<Int, Project>,
    contentPadding: PaddingValues,
    navToProject: (Int) -> Unit,
    navToEditRecord: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    val isTiming by isTimingFlow.collectAsState(false)

    val listState = rememberLazyListState()
    val timingProject by timingProjectFlow.collectAsState()
    val startTime by startTimeFlow.collectAsState()
    if (records.isNotEmpty()) {
        LazyColumn(
            modifier = modifier.padding(contentPadding),
            state = listState,
        ) {
            item {
                TimingCard(
                    timingProject,
                    startTime,
                    isTiming,
                )
            }
            records.forEach { (date, data) ->
                stickyHeader(key = date) {
                    DateTitle(
                        date = TimeUtils.getDataString(date),
                        duration = time[date] ?: 0L,
                    )
                }
                items(data, key = { it.id }) { record ->
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
        }
    } else {
        NoData(stringResource(R.string.no_record), modifier = modifier.padding(contentPadding))
    }
}
