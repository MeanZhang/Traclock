package com.mean.traclock.ui.screens

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import com.mean.traclock.ui.EditProjectActivity
import com.mean.traclock.ui.components.RecordItem
import com.mean.traclock.ui.components.TimingCard
import com.mean.traclock.ui.components.TopBar
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
    context: Context,
    viewModel: MainViewModel,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    Content(
        context,
        App.projectName,
        viewModel.projectsTime,
        App.isTiming,
        App.startTime,
        App.projects,
        contentPadding
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Content(
    context: Context?,
    timingProjectFlow: MutableStateFlow<String>,
    projectsTimeFlow: Flow<List<Record>>,
    isTimingFlow: StateFlow<Boolean>,
    startTimeFlow: MutableStateFlow<Long>,
    projects: MutableMap<String, Int>,
    contentPadding: PaddingValues
) {
    val isTiming by isTimingFlow.collectAsState(false)
    val projectsTime by projectsTimeFlow.collectAsState(listOf())
    val decayAnimationSpec = rememberSplineBasedDecay<Float>()
    val scrollBehavior = remember(decayAnimationSpec) {
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(decayAnimationSpec)
    }
    Scaffold(
        topBar = {
            TopBar(
                title = stringResource(R.string.projects),
                scrollBehavior = scrollBehavior,
                actions = {
                    IconButton(onClick = {
                        context?.startActivity(
                            Intent(context, EditProjectActivity::class.java)
                        )
                    }) {
                        Icon(Icons.Default.Add, stringResource(R.string.new_project))
                    }
                }
            )
        },
        modifier = Modifier
            .padding(contentPadding)
            .nestedScroll(scrollBehavior.nestedScrollConnection)
    ) {
        LazyColumn(contentPadding = it, modifier = Modifier.fillMaxSize()) {
            item {
                TimingCard(
                    timingProjectFlow.value,
                    startTimeFlow.value,
                    isTiming
                )
            }
            projectsTime.forEach {
                item {
                    RecordItem(context, it, Color(projects[it.project] ?: 0), false)
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showSystemUi = true)
@Composable
fun PreviewProjects() {
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
        context = null,
        timingProjectFlow = timingProjectFlow,
        projectsTimeFlow = projectsTimeFlow,
        isTimingFlow = isTimingFlow,
        startTimeFlow = startTimeFlow,
        projects = projects,
        contentPadding = PaddingValues()
    )
}
