package com.mean.traclock.ui.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import co.touchlab.kermit.Logger
import com.mean.traclock.ui.Constants.HORIZONTAL_MARGIN
import com.mean.traclock.ui.navigation.HomeRoute
import com.mean.traclock.utils.TimeUtils
import com.mean.traclock.utils.toPercentage
import com.mean.traclock.viewmodels.StatisticViewModel
import io.github.koalaplot.core.pie.DefaultSlice
import io.github.koalaplot.core.pie.PieChart
import io.github.koalaplot.core.util.ExperimentalKoalaPlotApi
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import ui.components.HomeScaffold

@Composable
fun Statistics(
    viewModel: StatisticViewModel,
    modifier: Modifier = Modifier,
) {
    HomeScaffold(route = HomeRoute.STATISTICS) { contentPadding ->
        Content(
            viewModel,
            contentPadding,
            modifier = modifier,
        )
    }
}

@OptIn(ExperimentalKoalaPlotApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun Content(
    viewModel: StatisticViewModel,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
) {
    var selectedPeriod by remember {
        mutableStateOf(
            Period(
                PeriodType.DAY,
            ),
        )
    }
    val projectsTime by viewModel.getProjectsTimeOfPeriod(selectedPeriod).collectAsState(listOf())
    val recordsNumber by viewModel.getRecordsNumber(selectedPeriod).collectAsState(0)
    val duration = projectsTime.sumOf { it.endTime }
    val data = projectsTime.map { (it.endTime).toFloat() }
    var selectedProjectIndex by remember(selectedPeriod) { mutableIntStateOf(-1) }
    val scrollableState = rememberScrollState()
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier.padding(contentPadding)) {
        PrimaryTabRow(selectedTabIndex = selectedPeriod.type.ordinal) {
            PeriodType.entries.forEach {
                Tab(
                    selected = selectedPeriod.type == it,
                    onClick = { selectedPeriod = selectedPeriod.changeType(it) },
                    text = { Text(it.label) },
                )
            }
        }
        Column(
            modifier = Modifier.verticalScroll(scrollableState),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (selectedPeriod.type != PeriodType.ALL_TIME) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier =
                        Modifier.padding(
                            HORIZONTAL_MARGIN,
                        ).fillMaxWidth(),
                ) {
                    val iconSize = 24.dp
                    IconButton(
                        onClick = {
                            selectedPeriod = selectedPeriod.prev
                        },
                        modifier = Modifier.size(iconSize),
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = "上一个",
                        )
                    }
                    Text(selectedPeriod.getString())
                    IconButton(
                        enabled = selectedPeriod.hasNext,
                        onClick = {
                            selectedPeriod = selectedPeriod.next
                        },
                        modifier = Modifier.size(iconSize),
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = "下一个",
                        )
                    }
                }
            }
            Row {
                SimpleStatisticItem("记录数", recordsNumber.toString(), modifier = Modifier.weight(1f))
                SimpleStatisticItem("时长", TimeUtils.getDurationString(duration), modifier = Modifier.weight(1f))
            }
            if (selectedPeriod.type == PeriodType.DAY) {
            } else {
            }
            HorizontalDivider()
            if (projectsTime.isNotEmpty()) {
                PieChart(
                    values = data,
                    labelConnector = {},
                    slice = { index ->
                        val project = viewModel.projects[projectsTime[index % projectsTime.size].project]
                        val color =
                            Color(
                                project?.color ?: 0,
                            )
                        DefaultSlice(
                            color = color,
                            gap = if (data.size > 1) 0.5f else 0f,
                            clickable = true,
                            hoverExpandFactor = 1.05f,
                            hoverElement = {
                                Card {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier.padding(4.dp),
                                    ) {
                                        viewModel.projects[projectsTime[index % projectsTime.size].project]?.let {
                                            val endTime = projectsTime[index % projectsTime.size].endTime
                                            Text(it.name, color = MaterialTheme.colorScheme.primary)
                                            Text(
                                                "${(endTime.toFloat() / projectsTime.sumOf { it.endTime }).toPercentage()}  ${
                                                    TimeUtils.getDurationString(
                                                        endTime,
                                                    )
                                                }",
                                            )
                                        }
                                    }
                                }
                            },
                            onClick = {
                                if (selectedProjectIndex == index) {
                                    selectedProjectIndex = -1
                                } else {
                                    selectedProjectIndex = index
                                }
                            },
                        )
                    },
                    holeSize = 0.75F,
                    modifier = Modifier.padding(8.dp),
                    forceCenteredPie = true,
                )
            }
            projectsTime.forEachIndexed { index, project ->
                var fontWeight by remember { mutableStateOf(FontWeight.Normal) }
                var color by remember { mutableStateOf(Color.Black) }
                if (index == selectedProjectIndex) {
                    fontWeight = FontWeight.Bold
                    color = MaterialTheme.colorScheme.primary
                } else {
                    fontWeight = FontWeight.Normal
                    color = MaterialTheme.colorScheme.onSurface
                }
                Row(
                    Modifier
                        .clickable {
                            if (selectedProjectIndex == index) {
                                selectedProjectIndex = -1
                            } else {
                                selectedProjectIndex = index
                            }
                        }
                        .padding(horizontal = HORIZONTAL_MARGIN, vertical = 6.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = Icons.Default.Circle,
                        contentDescription = null,
                        tint = Color(viewModel.projects[project.project]?.color ?: 0),
                        modifier =
                            Modifier
                                .size(20.dp)
                                .padding(end = 8.dp),
                    )
                    Text(
                        viewModel.projects[project.project]?.name ?: "",
                        fontWeight = fontWeight,
                        color = color,
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        ((project.endTime).toFloat() / projectsTime.sumOf { it.endTime }).toPercentage(),
                        fontWeight = fontWeight,
                        color = color,
                        modifier = Modifier.padding(end = 16.dp),
                    )
                    Text(
                        TimeUtils.getDurationString(project.endTime),
                        fontWeight = fontWeight,
                        fontFamily = FontFamily.Monospace,
                        color = color,
                    )
                }
            }
        }
    }
}

@Composable
private fun SimpleStatisticItem(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            description,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(horizontal = 16.dp).padding(top = 16.dp, bottom = 4.dp),
        )
        Text(
            title,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.padding(horizontal = 16.dp).padding(top = 4.dp, bottom = 16.dp),
        )
    }
}

@Composable
private fun DayTimeline() {
    Column {
        Row {
            Text("00:00")
            Spacer(modifier = Modifier.weight(1f))
            Text("12:00")
            Spacer(modifier = Modifier.weight(1f))
            Text("24:00")
        }
    }
}

@Composable
private fun WeekTrend() {
    Column {
        Row {
            Text("周一")
            Spacer(modifier = Modifier.weight(1f))
            Text("周三")
            Spacer(modifier = Modifier.weight(1f))
            Text("周五")
            Spacer(modifier = Modifier.weight(1f))
            Text("周日")
        }
    }
}

@Composable
private fun MonthTrend() {
    Column {
        Row {
            Text("1号")
            Spacer(modifier = Modifier.weight(1f))
            Text("10号")
            Spacer(modifier = Modifier.weight(1f))
            Text("20号")
            Spacer(modifier = Modifier.weight(1f))
            Text("30号")
        }
    }
}

@Composable
private fun AllTimeTrend() {
    Column {
        Row {
            Text("2021")
            Spacer(modifier = Modifier.weight(1f))
            Text("2022")
            Spacer(modifier = Modifier.weight(1f))
            Text("2023")
            Spacer(modifier = Modifier.weight(1f))
            Text("2024")
        }
    }
}

enum class PeriodType(val label: String) {
    DAY("天"),
    WEEK("周"),
    MONTH("月"),
    ALL_TIME("全部"),
}

data class Period(
    val type: PeriodType,
    val startDate: LocalDate = TimeUtils.getToday(),
    val endDate: LocalDate = TimeUtils.getToday(),
) {
    fun changeType(newType: PeriodType): Period {
        Logger.d("切换周期类型为${newType.label}")
        var newPeriod = this.copy(type = newType)
        val today = TimeUtils.getToday()
        when (newType) {
            PeriodType.DAY -> {
                newPeriod =
                    if (endDate > today) {
                        newPeriod.copy(startDate = today, endDate = today)
                    } else {
                        newPeriod.copy(startDate = endDate, endDate = endDate)
                    }
            }

            PeriodType.WEEK -> {
                val start = TimeUtils.getMonday(if (endDate > today) today else endDate)
                Logger.d("start: $start")
                val end = if (endDate.plus(1, DateTimeUnit.WEEK) >= today) today else start.plus(6, DateTimeUnit.DAY)
                Logger.d("end: $end")
                newPeriod = newPeriod.copy(startDate = start, endDate = end)
            }

            PeriodType.MONTH -> {
                val start = TimeUtils.getFirstDayOfMonth(if (endDate > today) today else endDate)
                Logger.d("start: $start")
                val end = if (endDate >= today) today else TimeUtils.getLastDayOfMonth(start)
                Logger.d("end: $end")
                newPeriod = newPeriod.copy(startDate = start, endDate = end)
            }

            PeriodType.ALL_TIME -> {}
        }
        return newPeriod
    }

    val prev: Period
        get() =
            when (type) {
                PeriodType.DAY -> {
                    val unit = DateTimeUnit.DAY
                    val date = startDate.minus(1, unit)
                    copy(startDate = date, endDate = date)
                }

                PeriodType.WEEK -> {
                    val unit = DateTimeUnit.WEEK
                    val start = startDate.minus(1, unit)
                    val end = start.plus(6, DateTimeUnit.DAY)
                    copy(startDate = start, endDate = end)
                }

                PeriodType.MONTH -> {
                    val start = startDate.minus(1, DateTimeUnit.MONTH)
                    copy(startDate = start, endDate = TimeUtils.getLastDayOfMonth(start))
                }

                PeriodType.ALL_TIME -> this
            }

    val next: Period
        get() {
            val today = TimeUtils.getToday()
            return when (type) {
                PeriodType.DAY -> {
                    val unit = DateTimeUnit.DAY
                    copy(startDate = startDate.plus(1, unit), endDate = endDate.plus(1, unit))
                }

                PeriodType.WEEK -> {
                    val unit = DateTimeUnit.WEEK
                    val start = startDate.plus(1, unit)
                    Logger.d("start: $start")
                    val end =
                        if (endDate.plus(1, DateTimeUnit.WEEK) >= today) today else start.plus(6, DateTimeUnit.DAY)
                    Logger.d("end: $end")
                    copy(startDate = start, endDate = end)
                }

                PeriodType.MONTH -> {
                    val start = startDate.plus(1, DateTimeUnit.MONTH)
                    Logger.d("start: $start")
                    val end =
                        if (endDate.plus(1, DateTimeUnit.MONTH) >= today) today else TimeUtils.getLastDayOfMonth(start)
                    Logger.d("end: $end")
                    copy(startDate = start, endDate = end)
                }

                PeriodType.ALL_TIME -> this
            }
        }

    val hasNext: Boolean
        get() =
            when (type) {
                PeriodType.DAY -> TimeUtils.getToday() > endDate
                PeriodType.WEEK -> TimeUtils.getToday() > endDate
                PeriodType.MONTH -> TimeUtils.getToday() > endDate
                PeriodType.ALL_TIME -> false
            }

    fun getString(): String =
        when (type) {
            PeriodType.DAY -> TimeUtils.getDisplayDate(startDate)
            PeriodType.WEEK -> TimeUtils.getDisplayPeriod(startDate, endDate)
            PeriodType.MONTH -> TimeUtils.getMonthString(startDate)
            PeriodType.ALL_TIME -> "全部"
        }
}
