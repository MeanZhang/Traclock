package com.mean.traclock.ui.screens

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mean.traclock.App
import com.mean.traclock.R
import com.mean.traclock.database.Record
import com.mean.traclock.ui.components.DateTitle
import com.mean.traclock.ui.components.RecordItem
import com.mean.traclock.ui.components.TimingCard
import com.mean.traclock.ui.components.TopBar
import com.mean.traclock.utils.getDataString
import com.mean.traclock.viewmodels.MainViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import java.time.ZoneId
import java.time.ZonedDateTime

@Composable
fun TimeLine(
    context: Context,
    viewModel: MainViewModel,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    val detailView = remember { mutableStateOf(true) }
    val recordsFlow = if (detailView.value) viewModel.records else viewModel.projectsTimeOfDays
    Content(
        context,
        recordsFlow,
        viewModel.timeOfDays,
        detailView,
        App.isTiming,
        App.projectName,
        App.startTime,
        App.projects,
        contentPadding
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
private fun Content(
    context: Context?,
    recordsFlow: Flow<Map<Int, List<Record>>>,
    timeFlow: Flow<Map<Int, Long>>,
    detailView: MutableState<Boolean>,
    isTimingFlow: StateFlow<Boolean>,
    timingProjectFlow: MutableStateFlow<String>,
    startTimeFlow: MutableStateFlow<Long>,
    projects: MutableMap<String, Int>,
    contentPadding: PaddingValues
) {
    val isTiming by isTimingFlow.collectAsState(false)
    val records by recordsFlow.collectAsState(mapOf())
    val time by timeFlow.collectAsState(mapOf())
    val decayAnimationSpec = rememberSplineBasedDecay<Float>()
    val state = rememberTopAppBarScrollState()
    val scrollBehavior = remember(decayAnimationSpec) {
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(decayAnimationSpec, state)
    }
    Scaffold(
        topBar = {
            TopBar(
                title = stringResource(R.string.timeline),
                scrollBehavior = scrollBehavior,
                actions = {
                    IconButton(onClick = {
                        detailView.value = !detailView.value
                    }) {
                        Icon(Icons.Default.SwapHoriz, stringResource(R.string.change_view))
                    }
                }
            )
        },
        modifier = Modifier
            .padding(contentPadding)
            .nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { innerPadding ->
        val listState = rememberLazyListState()
        LazyColumn(Modifier.padding(innerPadding), state = listState) {
            item {
                TimingCard(
                    timingProjectFlow.value,
                    startTimeFlow.value,
                    isTiming
                )
                rememberCoroutineScope().launch {
                    listState.animateScrollToItem(0)
                }
            }
            records.forEach { (date, data) ->
                stickyHeader {
                    DateTitle(
                        date = getDataString(date),
                        duration = time[date] ?: 0L
                    )
                }
                items(data) { record ->
                    RecordItem(
                        context = context,
                        record = record,
                        color = Color(projects[record.project] ?: 0),
                        detailView = detailView.value,
                        listState = listState
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showSystemUi = true)
@Composable
fun PreviewTimeline() {
    val projects = mutableMapOf(
        Pair("测试1", Color.Black.toArgb()),
        Pair("测试2", Color.Blue.toArgb()),
        Pair("测试3", Color.Cyan.toArgb()),
        Pair("测试4", Color.DarkGray.toArgb())
    )
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
        detailView = remember { mutableStateOf(true) },
        isTimingFlow = isTimingFlow,
        timingProjectFlow = timingProjectFlow,
        startTimeFlow = startTimeFlow,
        projects,
        contentPadding = PaddingValues()
    )
}
