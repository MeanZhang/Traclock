package com.mean.traclock.ui.screens

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
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.nestedscroll.nestedScroll
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
import com.mean.traclock.App
import com.mean.traclock.R
import com.mean.traclock.database.AppDatabase
import com.mean.traclock.database.Record
import com.mean.traclock.ui.components.NoData
import com.mean.traclock.ui.components.TopBar
import com.mean.traclock.util.HORIZONTAL_MARGIN
import com.mean.traclock.util.getDurationString
import com.mean.traclock.util.getIntDate
import com.mean.traclock.util.log

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Statistics(contentPadding: PaddingValues = PaddingValues(0.dp)) {
    val date = getIntDate(System.currentTimeMillis())
    val projectsTime by AppDatabase.getDatabase(App.context).recordDao().getProjectsTimeOfDate(date)
        .collectAsState(listOf())
    val duration = projectsTime.sumOf { (it.endTime - it.startTime) / 1000 }
    val selected = remember { mutableStateOf(-1) }
    val scrollBehavior = remember { TopAppBarDefaults.pinnedScrollBehavior() }
    Scaffold(
        topBar = {
            TopBar(
                title = stringResource(R.string.statistics),
                scrollBehavior = scrollBehavior
            )
        },
        modifier = Modifier
            .padding(contentPadding)
            .nestedScroll(scrollBehavior.nestedScrollConnection),
    ) {
        if (duration > 0) {
            Column(
                Modifier
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    Modifier
                        .padding(horizontal = HORIZONTAL_MARGIN)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(stringResource(R.string.records_duration))
                    Text(getDurationString(duration))
                }
                AndroidView(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp)
                        .padding(horizontal = HORIZONTAL_MARGIN),
                    factory = { context ->
                        PieChart(context).apply {
                            setPieChart(this, projectsTime, duration, selected)
                        }
                    },
                    update = { chart -> setPieChart(chart, projectsTime, duration, selected) }
                )
                for ((index, project) in projectsTime.withIndex()) {
                    var fontWeight by remember { mutableStateOf(FontWeight.Medium) }
                    var color by remember { mutableStateOf(Color.Black) }
                    if (index == selected.value) {
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
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                imageVector = Icons.Default.Circle,
                                contentDescription = null,
                                tint = Color(App.projectsList[project.project] ?: 0),
                                modifier = Modifier
                                    .size(20.dp)
                                    .padding(horizontal = 4.dp)
                            )
                            Text(
                                project.project,
                                fontWeight = fontWeight,
                                color = color
                            )
                        }
                        Text(
                            getDurationString(project.startTime, project.endTime),
                            fontWeight = fontWeight,
                            color = color
                        )
                    }
                }
            }
        } else {
            NoData()
        }
    }
}

fun setPieChart(
    chart: PieChart,
    projectsTime: List<Record>,
    duration: Long,
    selected: MutableState<Int>
) {
    log(projectsTime.toString())
    chart.minimumHeight = chart.width // 宽高相同
    chart.description.isEnabled = false // 不显示description
    chart.setHoleColor(Color.Transparent.toArgb()) // 中间圆孔设为透明
    chart.centerText = getDurationString(duration)
    chart.legend.isEnabled = false // 不显示图例
    chart.setUsePercentValues(true)
    chart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
        override fun onValueSelected(e: Entry?, h: Highlight?) {
            if (h != null) {
                selected.value = h.x.toInt()
            }
        }

        override fun onNothingSelected() {
            selected.value = -1
        }
    })

    val list = projectsTime.map { PieEntry((it.endTime - it.startTime).toFloat(), it.project) }
    val dataset = PieDataSet(list, App.context.getString(R.string.records_duration))
    dataset.colors = projectsTime.map { App.projectsList[it.project] }
    dataset.valueFormatter = PercentFormatter(chart)
    chart.data = PieData(dataset)
    chart.animateXY(1000, 1000)
}
