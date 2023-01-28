package com.mean.traclock.ui.screens

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mean.traclock.data.DataModel
import com.mean.traclock.database.Project
import com.mean.traclock.database.Record
import com.mean.traclock.ui.components.DateTitle
import com.mean.traclock.ui.components.RecordItem
import com.mean.traclock.ui.components.TimingCard
import com.mean.traclock.utils.TimeUtils
import com.mean.traclock.viewmodels.MainViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import java.time.ZoneId
import java.time.ZonedDateTime

@Composable
fun TimeLine(
    context: Context,
    viewModel: MainViewModel,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    val detailView by viewModel.detailView.collectAsState()
    val recordsFlow = if (detailView) viewModel.records else viewModel.projectsTimeOfDays
    Content(
        context,
        recordsFlow,
        viewModel.timeOfDays,
        detailView,
        DataModel.dataModel.isRunning,
        DataModel.dataModel.projectName,
        DataModel.dataModel.startTime,
        DataModel.dataModel.projects,
        contentPadding
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun Content(
    context: Context?,
    recordsFlow: Flow<Map<Int, List<Record>>>,
    timeFlow: Flow<Map<Int, Long>>,
    detailView: Boolean,
    isTimingFlow: StateFlow<Boolean>,
    timingProjectFlow: StateFlow<String>,
    startTimeFlow: StateFlow<Long>,
    projects: Map<String, Project>,
    contentPadding: PaddingValues
) {
    val isTiming by isTimingFlow.collectAsState(false)
    val records by recordsFlow.collectAsState(mapOf())
    val time by timeFlow.collectAsState(mapOf())

    val listState = rememberLazyListState()
    val timingProject by timingProjectFlow.collectAsState()
    val startTime by startTimeFlow.collectAsState()
    LazyColumn(
        contentPadding = contentPadding,
        state = listState
    ) {
        item {
            TimingCard(
                timingProject,
                startTime,
                isTiming
            )
        }
        records.forEach { (date, data) ->
            stickyHeader {
                DateTitle(
                    date = TimeUtils.getDataString(date),
                    duration = time[date] ?: 0L
                )
            }
            items(data) { record ->
                RecordItem(
                    context = context,
                    record = record,
                    color = Color(projects[record.project]?.color ?: 0),
                    detailView = detailView,
                    listState = listState
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showSystemUi = true)
@Composable
fun PreviewTimeline() {
    val projects = listOf(
        Project("测试1", Color.Black.toArgb()),
        Project("测试2", Color.Blue.toArgb()),
        Project("测试3", Color.Cyan.toArgb()),
        Project("测试4", Color.DarkGray.toArgb())
    ).associateBy { it.name }
    val records = mutableListOf<Record>()
    val now = ZonedDateTime.now(ZoneId.systemDefault())
    for (i in 0..10) {
        for (j in 0..10) {
            val startTime =
                now.minusDays(i.toLong()).minusHours(i.toLong())
                    .minusMinutes((j * 30).toLong())
            val endTime =
                startTime.plusMinutes((j * 2).toLong()).plusSeconds(((i + j) * 3 + 2).toLong())
            records.add(
                Record(
                    projects.keys.elementAt(j % projects.size),
                    startTime.toInstant().toEpochMilli(),
                    endTime.toInstant().toEpochMilli()
                )
            )
        }
    }
    val map = records.groupBy { it.date }
    val time = map.mapValues { (_, value) ->
        value.sumOf { (it.endTime - it.startTime) / 1000 }
    }
    val recordsFlow = flowOf(map)
    val timeFlow = flowOf(time)
    val isTimingFlow = MutableStateFlow(false)
    val timingProjectFlow = MutableStateFlow(projects.keys.elementAt(0))
    val startTimeFlow = MutableStateFlow(System.currentTimeMillis() - 1000 * 10)
    Content(
        context = null,
        recordsFlow = recordsFlow,
        timeFlow = timeFlow,
        detailView = true,
        isTimingFlow = isTimingFlow,
        timingProjectFlow = timingProjectFlow,
        startTimeFlow = startTimeFlow,
        projects,
        contentPadding = PaddingValues()
    )
}
