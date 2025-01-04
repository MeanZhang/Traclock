package com.mean.traclock.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import co.touchlab.kermit.Logger
import com.mean.traclock.data.Record
import com.mean.traclock.ui.Constants.HORIZONTAL_MARGIN
import com.mean.traclock.ui.navigation.HomeRoute
import com.mean.traclock.utils.TimeUtils
import com.mean.traclock.utils.TimeUtils.toInt
import com.mean.traclock.utils.toPercentage
import com.mean.traclock.viewmodels.StatisticViewModel
import data.Project
import io.github.koalaplot.core.bar.DefaultVerticalBar
import io.github.koalaplot.core.bar.DefaultVerticalBarPlotEntry
import io.github.koalaplot.core.bar.DefaultVerticalBarPosition
import io.github.koalaplot.core.bar.VerticalBarPlot
import io.github.koalaplot.core.pie.DefaultSlice
import io.github.koalaplot.core.pie.PieChart
import io.github.koalaplot.core.util.ExperimentalKoalaPlotApi
import io.github.koalaplot.core.util.generateHueColorPalette
import io.github.koalaplot.core.xygraph.XYGraph
import io.github.koalaplot.core.xygraph.rememberIntLinearAxisModel
import io.github.koalaplot.core.xygraph.rememberLongLinearAxisModel
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
            when (selectedPeriod.type) {
                PeriodType.DAY -> {
                    val records by viewModel.getRecords(selectedPeriod.startDate).collectAsState(emptyList())
                    DayTimeline(records, viewModel.projects)
                }

                PeriodType.WEEK -> {
                    val records by viewModel.getRecords(selectedPeriod).collectAsState(emptyList())
                    WeekTrend(records)
                }

                PeriodType.MONTH -> {
                    val records by viewModel.getRecords(selectedPeriod).collectAsState(emptyList())
                    MonthTrend(selectedPeriod, records)
                }

                PeriodType.ALL_TIME -> {
                    val durations by viewModel.getDurationsOfYears().collectAsState(emptyMap())
                    AllTimeTrend(durations)
                }
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
                    Modifier.clickable {
                        if (selectedProjectIndex == index) {
                            selectedProjectIndex = -1
                        } else {
                            selectedProjectIndex = index
                        }
                    }.padding(horizontal = HORIZONTAL_MARGIN, vertical = 6.dp).fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = Icons.Default.Circle,
                        contentDescription = null,
                        tint = Color(viewModel.projects[project.project]?.color ?: 0),
                        modifier = Modifier.size(20.dp).padding(end = 8.dp),
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
private fun DayTimeline(
    records: List<Record>,
    projects: Map<Long, Project>,
    modifier: Modifier = Modifier,
) {
    val totalMillis = 24 * 60 * 60 * 1000
    Logger.d("records: $records")
    val height = 24.dp
    val freeColor = MaterialTheme.colorScheme.inverseOnSurface
    Column(modifier = modifier.padding(vertical = 8.dp)) {
        Row {
            if (records.isEmpty()) {
                Spacer(
                    modifier = Modifier.weight(1f).height(height).background(freeColor),
                )
            } else {
                Spacer(
                    modifier =
                        Modifier.weight((TimeUtils.getMillisOfDay(records[0].startTime)) / totalMillis.toFloat())
                            .height(height)
                            .background(freeColor),
                )
                records.forEachIndexed { index, it ->
                    Spacer(
                        modifier =
                            Modifier.weight((it.endTime - it.startTime) / totalMillis.toFloat()).height(height)
                                .background(Color(projects[it.project]?.color ?: 0)),
                    )
                    if (index < records.size - 1) {
                        if (records[index + 1].startTime > it.endTime) {
                            Spacer(
                                modifier =
                                    Modifier.weight((records[index + 1].startTime - it.endTime) / totalMillis.toFloat())
                                        .height(height)
                                        .background(freeColor),
                            )
                        }
                    }
                }
                Spacer(
                    modifier =
                        Modifier.weight((totalMillis - TimeUtils.getMillisOfDay(records.last().endTime)) / totalMillis.toFloat())
                            .height(height)
                            .background(freeColor),
                )
            }
        }
        Row {
            val color = MaterialTheme.colorScheme.outline
            val style = MaterialTheme.typography.bodySmall
            Text("0", color = color, style = style)
            Spacer(modifier = Modifier.weight(1f))
            Text("6", color = color, style = style)
            Spacer(modifier = Modifier.weight(1f))
            Text("12", color = color, style = style)
            Spacer(modifier = Modifier.weight(1f))
            Text("18", color = color, style = style)
            Spacer(modifier = Modifier.weight(1f))
            Text("24", color = color, style = style)
        }
    }
}

@Composable
private fun WeekTrend(
    records: List<Record>,
    modifier: Modifier = Modifier,
) {
    val data =
        records.groupBy { it.date }.mapKeys { TimeUtils.getDayOfWeek(it.key) }
            .mapValues { (_, records) -> records.sumOf { it.endTime - it.startTime } }
    val maxTime = data.values.maxOrNull() ?: 1

    fun xLables(date: Int): String {
        if (date < 1 || date > 7) return ""
        return TimeUtils.CHINESE_DAY_OF_WEEK_NAMES.names[date - 1]
    }

    Chart(0..8, maxTime, data, ::xLables, modifier)
}

@Composable
private fun MonthTrend(
    period: Period,
    records: List<Record>,
    modifier: Modifier = Modifier,
) {
    val data = records.groupBy { it.date }.mapValues { (_, records) -> records.sumOf { it.endTime - it.startTime } }
    val days = period.startDate.toInt()..period.endDate.toInt()
    val maxTime = data.values.maxOrNull() ?: 1

    fun xLables(date: Int): String {
        val localDate = TimeUtils.getDate(date)
        return "${localDate.dayOfMonth}日"
    }
    Chart(days, maxTime, data, ::xLables, modifier)
}

@Composable
private fun AllTimeTrend(
    durations: Map<Int, Long>,
    modifier: Modifier = Modifier,
) {
    if (durations.isNotEmpty()) {
        val days = durations.keys.last() - 2..durations.keys.first() + 1
        val maxTime = durations.values.max()

        fun xLables(year: Int): String {
            return "${year}年"
        }
        Chart(days, maxTime, durations, ::xLables, modifier)
    }
}

@OptIn(ExperimentalKoalaPlotApi::class)
@Composable
private fun Chart(
    days: IntRange,
    maxTime: Long,
    data: Map<Int, Long>,
    xLables: (Int) -> String,
    modifier: Modifier = Modifier,
) {
    val colors = generateHueColorPalette(data.size)
    // FIXME remove after koalaplot update
    val zoomRangeLimit = if (maxTime < 5) 1 else (maxTime * 0.2).toLong()
    XYGraph(
        xAxisModel = rememberIntLinearAxisModel(days, zoomRangeLimit = 1),
        yAxisModel = rememberLongLinearAxisModel(0..maxTime, zoomRangeLimit = zoomRangeLimit),
        xAxisLabels = xLables,
        yAxisLabels = TimeUtils::getDurationString,
        modifier = modifier.height(200.dp).padding(8.dp),
    ) {
        VerticalBarPlot(
            data =
                data.map {
                    DefaultVerticalBarPlotEntry(
                        x = it.key,
                        y = DefaultVerticalBarPosition(yMin = 0, yMax = it.value),
                    )
                },
            bar = {
                DefaultVerticalBar(SolidColor(colors[it]))
            },
        )
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
                val end = start.plus(6, DateTimeUnit.DAY)
                newPeriod = newPeriod.copy(startDate = start, endDate = end)
            }

            PeriodType.MONTH -> {
                val start = TimeUtils.getFirstDayOfMonth(if (endDate > today) today else endDate)
                val end = TimeUtils.getLastDayOfMonth(start)
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
                    val end = start.plus(6, DateTimeUnit.DAY)
                    copy(startDate = start, endDate = end)
                }

                PeriodType.MONTH -> {
                    val start = startDate.plus(1, DateTimeUnit.MONTH)
                    val end = TimeUtils.getLastDayOfMonth(start)
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
