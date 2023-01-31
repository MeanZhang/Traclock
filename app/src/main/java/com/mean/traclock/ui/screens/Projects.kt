package com.mean.traclock.ui.screens

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mean.traclock.data.DataModel
import com.mean.traclock.database.Project
import com.mean.traclock.database.Record
import com.mean.traclock.ui.components.RecordItem
import com.mean.traclock.ui.components.TimingCard
import com.mean.traclock.viewmodels.MainViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import java.time.ZoneId
import java.time.ZonedDateTime

@DelicateCoroutinesApi
@Composable
fun Projects(
    viewModel: MainViewModel,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    Content(
        DataModel.dataModel.projectName,
        viewModel.projectsTime,
        DataModel.dataModel.isRunning,
        DataModel.dataModel.startTime,
        DataModel.dataModel.projects,
        contentPadding
    )
}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
private fun Content(
    timingProjectFlow: StateFlow<String>,
    projectsTimeFlow: Flow<List<Record>>,
    isTimingFlow: StateFlow<Boolean>,
    startTimeFlow: StateFlow<Long>,
    projects: Map<String, Int>,
    contentPadding: PaddingValues
) {
    val isTiming by isTimingFlow.collectAsState(false)
    val projectsTime by projectsTimeFlow.collectAsState(listOf())

    LazyColumn(contentPadding = contentPadding, modifier = Modifier.fillMaxSize()) {
        item {
            TimingCard(
                timingProjectFlow.value,
                startTimeFlow.value,
                isTiming
            )
        }
        projectsTime.forEach {
            item {
                RecordItem(it, Color(projects[it.project] ?: 0), false)
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showSystemUi = true)
@Composable
fun PreviewProjects() {
    val projects = listOf(
        Project("测试1", Color.Black.toArgb()),
        Project("测试2", Color.Blue.toArgb()),
        Project("测试3", Color.Cyan.toArgb()),
        Project("测试4", Color.DarkGray.toArgb())
    ).associate { Pair(it.name, it.color) }
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
    val timingProjectFlow = MutableStateFlow(projects.keys.elementAt(0))
    val projectsTimeFlow = flowOf(
        records.groupBy { it.project }
            .mapValues { (key, value) ->
                Record(key, 0, value.sumOf { (it.endTime - it.startTime) / 1000 }, 0)
            }.values.toList()
    )
    val isTimingFlow = MutableStateFlow(true)
    val startTimeFlow = MutableStateFlow(System.currentTimeMillis() - 1000 * 10)
    Content(
        timingProjectFlow = timingProjectFlow,
        projectsTimeFlow = projectsTimeFlow,
        isTimingFlow = isTimingFlow,
        startTimeFlow = startTimeFlow,
        projects = projects,
        contentPadding = PaddingValues()
    )
}