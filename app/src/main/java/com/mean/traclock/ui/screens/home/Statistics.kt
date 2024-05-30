package com.mean.traclock.ui.screens.home

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.mean.traclock.R
import com.mean.traclock.data.Project
import com.mean.traclock.data.Record
import com.mean.traclock.ui.Constants.HORIZONTAL_MARGIN
import com.mean.traclock.ui.components.HomeScaffold
import com.mean.traclock.ui.components.NoData
import com.mean.traclock.ui.navigation.HomeRoute
import com.mean.traclock.utils.TimeUtils
import com.mean.traclock.viewmodels.MainViewModel
import kotlinx.coroutines.flow.Flow

@Composable
fun Statistics(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier,
) {
    val date = TimeUtils.getIntDate(System.currentTimeMillis())
    HomeScaffold(route = HomeRoute.STATISTICS) { contentPadding ->
        Content(
            viewModel,
            viewModel.getProjectsTimeOfDay(date),
            contentPadding,
            modifier = modifier,
        )
    }
}

@Composable
private fun Content(
    viewModel: MainViewModel,
    projectsTimeFlow: Flow<List<Record>>,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
) {
    val projectsTime by projectsTimeFlow.collectAsState(listOf())
    val duration = projectsTime.sumOf { it.endTime - it.startTime } / 1000
    val selected = remember { mutableIntStateOf(-1) }
    val context = LocalContext.current
    if (duration > 0) {
        Column(
            modifier
                .padding(contentPadding)
                .verticalScroll(rememberScrollState()),
        ) {
            Row(
                Modifier
                    .padding(horizontal = HORIZONTAL_MARGIN)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(stringResource(R.string.records_duration))
                Text(TimeUtils.getDurationString(duration))
            }
            AndroidView(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(400.dp)
                        .padding(horizontal = HORIZONTAL_MARGIN),
                factory = { context ->
                    PieChart(context).apply {
                        setPieChart(viewModel.projects, context, this, projectsTime, duration, selected)
                    }
                },
                update = { chart ->
                    setPieChart(
                        viewModel.projects,
                        context,
                        chart,
                        projectsTime,
                        duration,
                        selected,
                    )
                },
            )
            for ((index, project) in projectsTime.withIndex()) {
                var fontWeight by remember { mutableStateOf(FontWeight.Medium) }
                var color by remember { mutableStateOf(Color.Black) }
                if (index == selected.intValue) {
                    fontWeight = FontWeight.Bold
                    color = MaterialTheme.colorScheme.onSurface
                } else {
                    fontWeight = FontWeight.Normal
                    color = MaterialTheme.colorScheme.outline
                }
                Row(
                    Modifier
                        .padding(horizontal = HORIZONTAL_MARGIN, vertical = 8.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            imageVector = Icons.Default.Circle,
                            contentDescription = null,
                            tint = Color(viewModel.projects[project.project]?.color ?: 0),
                            modifier =
                                Modifier
                                    .size(20.dp)
                                    .padding(horizontal = 4.dp),
                        )
                        Text(
                            viewModel.projects[project.project]?.name ?: "",
                            fontWeight = fontWeight,
                            color = color,
                        )
                    }
                    Text(
                        TimeUtils.getDurationString(project.startTime, project.endTime),
                        fontWeight = fontWeight,
                        color = color,
                    )
                }
            }
        }
    } else {
        NoData(stringResource(R.string.no_record), modifier = modifier.padding(contentPadding))
    }
}

fun setPieChart(
    projects: Map<Long, Project>,
    context: Context,
    chart: PieChart,
    projectsTime: List<Record>,
    duration: Long,
    selected: MutableState<Int>,
) {
    chart.minimumHeight = chart.width // 宽高相同
    chart.description.isEnabled = false // 不显示description
    chart.setHoleColor(Color.Transparent.toArgb()) // 中间圆孔设为透明
    chart.centerText = TimeUtils.getDurationString(duration)
    chart.legend.isEnabled = false // 不显示图例
    chart.setUsePercentValues(true)
    chart.setOnChartValueSelectedListener(
        object : OnChartValueSelectedListener {
            override fun onValueSelected(
                e: Entry?,
                h: Highlight?,
            ) {
                if (h != null) {
                    selected.value = h.x.toInt()
                }
            }

            override fun onNothingSelected() {
                selected.value = -1
            }
        },
    )

    val list = projectsTime.map { PieEntry((it.endTime - it.startTime).toFloat(), it.project) }
    val dataset = PieDataSet(list, context.getString(R.string.records_duration))
    dataset.colors = projectsTime.map { projects[it.project]?.color ?: 0 }
    dataset.valueFormatter = PercentFormatter(chart)
    chart.data = PieData(dataset)
    chart.animateXY(1000, 1000)
}
