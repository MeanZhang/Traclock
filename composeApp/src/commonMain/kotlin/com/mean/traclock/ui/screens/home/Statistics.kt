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
import androidx.compose.runtime.LaunchedEffect
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
import com.mean.traclock.model.RecordWithProject
import com.mean.traclock.ui.Constants.HORIZONTAL_MARGIN
import com.mean.traclock.ui.navigation.HomeRoute
import com.mean.traclock.utils.TimeUtils
import com.mean.traclock.utils.toPercentage
import com.mean.traclock.viewmodels.StatisticViewModel
import com.patrykandpatrick.vico.multiplatform.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.multiplatform.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.multiplatform.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.multiplatform.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.multiplatform.cartesian.data.columnSeries
import com.patrykandpatrick.vico.multiplatform.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.multiplatform.cartesian.marker.ColumnCartesianLayerMarkerTarget
import com.patrykandpatrick.vico.multiplatform.cartesian.marker.DefaultCartesianMarker
import com.patrykandpatrick.vico.multiplatform.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.multiplatform.cartesian.rememberVicoScrollState
import com.patrykandpatrick.vico.multiplatform.common.component.rememberTextComponent
import io.github.koalaplot.core.pie.DefaultSlice
import io.github.koalaplot.core.pie.PieChart
import io.github.koalaplot.core.util.ExperimentalKoalaPlotApi
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import ui.components.HomeScaffold
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

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
    val projectsDuration by viewModel.getProjectsDuration(selectedPeriod).collectAsState(listOf())
    val recordsNumber by viewModel.getRecordsNumber(selectedPeriod).collectAsState(0)
    val duration = projectsDuration.sumOf { it.duration.inWholeMilliseconds }
    val data = projectsDuration.map { it.duration.inWholeMilliseconds.toFloat() }
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
                        Modifier
                            .padding(
                                HORIZONTAL_MARGIN,
                            )
                            .fillMaxWidth(),
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
                SimpleStatisticItem(
                    "时长",
                    TimeUtils.getDurationString(duration.toDuration(DurationUnit.MILLISECONDS)),
                    modifier = Modifier.weight(1f),
                )
            }
            when (selectedPeriod.type) {
                PeriodType.DAY -> {
                    val records by viewModel.getRecordsWithProject(selectedPeriod.startDate).collectAsState(emptyList())
                    DayTimeline(records)
                }

                PeriodType.WEEK -> {
                    val daysDuration by viewModel.watchDaysDuration(selectedPeriod).collectAsState(mapOf())
                    WeekTrend(selectedPeriod, daysDuration)
                }

                PeriodType.MONTH -> {
                    val daysDuration by viewModel.watchDaysDuration(selectedPeriod).collectAsState(mapOf())
                    MonthTrend(selectedPeriod, daysDuration)
                }

                PeriodType.YEAR -> {
                    val monthsDuration by viewModel.watchMonthsDuration(selectedPeriod.startDate.year)
                        .collectAsState(mapOf())
                    YearTrend(monthsDuration)
                }

                PeriodType.ALL_TIME -> {
                    val durations by viewModel.watchYearsDuration().collectAsState(emptyMap())
                    AllTimeTrend(durations)
                }
            }
            HorizontalDivider()
            if (projectsDuration.isNotEmpty()) {
                PieChart(
                    values = data,
                    labelConnector = {},
                    slice = { index ->
                        val projectDuration = projectsDuration[index % projectsDuration.size]
                        DefaultSlice(
                            color = projectDuration.color,
                            gap = if (data.size > 1) 0.5f else 0f,
                            clickable = true,
                            hoverExpandFactor = 1.05f,
                            hoverElement = {
                                Card {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier.padding(4.dp),
                                    ) {
                                        val endTime = projectDuration.duration.inWholeMilliseconds
                                        Text(projectDuration.name, color = MaterialTheme.colorScheme.primary)
                                        Text(
                                            "${
                                                (
                                                    endTime.toFloat() /
                                                        projectsDuration.sumOf {
                                                            it.duration.toLong(
                                                                DurationUnit.MILLISECONDS,
                                                            )
                                                        }
                                                ).toPercentage()
                                            }  ${
                                                TimeUtils.getDurationString(
                                                    endTime.toDuration(DurationUnit.MILLISECONDS),
                                                )
                                            }",
                                        )
                                    }
                                }
                            },
                            onClick = {
                                selectedProjectIndex =
                                    if (selectedProjectIndex == index) {
                                        -1
                                    } else {
                                        index
                                    }
                            },
                        )
                    },
                    holeSize = 0.75F,
                    modifier = Modifier.padding(8.dp),
                    forceCenteredPie = true,
                )
            }
            projectsDuration.forEachIndexed { index, projectDuration ->
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
                            selectedProjectIndex =
                                if (selectedProjectIndex == index) {
                                    -1
                                } else {
                                    index
                                }
                        }
                        .padding(horizontal = HORIZONTAL_MARGIN, vertical = 6.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = Icons.Default.Circle,
                        contentDescription = null,
                        tint = projectDuration.color,
                        modifier =
                            Modifier
                                .size(20.dp)
                                .padding(end = 8.dp),
                    )
                    Text(
                        projectDuration.name,
                        fontWeight = fontWeight,
                        color = color,
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        (
                            projectDuration.duration.inWholeMilliseconds
                                .toFloat() / projectsDuration.sumOf { it.duration.inWholeMilliseconds }
                        ).toPercentage(),
                        fontWeight = fontWeight,
                        color = color,
                        modifier = Modifier.padding(end = 16.dp),
                    )
                    Text(
                        TimeUtils.getDurationString(
                            projectDuration.duration.inWholeMilliseconds.toDuration(DurationUnit.MILLISECONDS),
                        ),
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
            modifier =
                Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 16.dp, bottom = 4.dp),
        )
        Text(
            title,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.secondary,
            modifier =
                Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 4.dp, bottom = 16.dp),
        )
    }
}

@Composable
private fun DayTimeline(
    records: List<RecordWithProject>,
    modifier: Modifier = Modifier,
) {
    val totalMillis = 24 * 60 * 60 * 1000
    val height = 24.dp
    val freeColor = MaterialTheme.colorScheme.inverseOnSurface
    Column(modifier = modifier.padding(vertical = 8.dp)) {
        Row {
            if (records.isEmpty()) {
                Spacer(
                    modifier =
                        Modifier
                            .weight(1f)
                            .height(height)
                            .background(freeColor),
                )
            } else {
                Spacer(
                    modifier =
                        Modifier
                            .weight((TimeUtils.getMillisOfDay(records[0].startTime)) / totalMillis.toFloat())
                            .height(height)
                            .background(freeColor),
                )
                records.forEachIndexed { index, it ->
                    Spacer(
                        modifier =
                            Modifier
                                .weight((it.endTime - it.startTime).inWholeMilliseconds / totalMillis.toFloat())
                                .height(height)
                                .background(it.color),
                    )
                    if (index < records.size - 1) {
                        if (records[index + 1].startTime > it.endTime) {
                            Spacer(
                                modifier =
                                    Modifier
                                        .weight(
                                            (records[index + 1].startTime - it.endTime).toLong(
                                                DurationUnit.MILLISECONDS,
                                            ) / totalMillis.toFloat(),
                                        )
                                        .height(height)
                                        .background(freeColor),
                            )
                        }
                    }
                }
                Spacer(
                    modifier =
                        Modifier
                            .weight((totalMillis - TimeUtils.getMillisOfDay(records.last().endTime)) / totalMillis.toFloat())
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
    period: Period,
    records: Map<LocalDate, Duration>,
    modifier: Modifier = Modifier,
) {
    val days = period.getDays()
    val data =
        days.associate {
            it.dayOfWeek.value to (records[it] ?: Duration.ZERO)
        }

    val xLables = { day: Int ->
        if (day < 1 || day > 7) {
            ""
        } else {
            TimeUtils.CHINESE_DAY_OF_WEEK_NAMES.names[day - 1]
        }
    }

    Chart(data, modifier, xLables)
}

@Composable
private fun MonthTrend(
    period: Period,
    records: Map<LocalDate, Duration>,
    modifier: Modifier = Modifier,
) {
    val days = period.getDays()
    val data =
        days.associate {
            it.dayOfMonth to (records[it] ?: Duration.ZERO)
        }
    Chart(data, modifier)
}

@Composable
private fun YearTrend(
    records: Map<Int, Duration>,
    modifier: Modifier = Modifier,
) {
    val months = (1..12)
    val data =
        months.associateWith { (records[it] ?: Duration.ZERO) }

    Chart(data, modifier)
}

@Composable
private fun AllTimeTrend(
    durations: Map<Int, Duration>,
    modifier: Modifier = Modifier,
) {
    val currentYear = TimeUtils.getCurrentYear()
    val years = ((durations.keys.lastOrNull() ?: currentYear) - 2)..(durations.keys.firstOrNull() ?: currentYear)
    val data = years.associateWith { (durations[it] ?: Duration.ZERO) }
    Chart(data, modifier)
}

@Composable
private fun Chart(
    data: Map<Int, Duration>,
    modifier: Modifier = Modifier,
    xLables: ((Int) -> String) = { it.toString() },
) {
    val scrollState = rememberVicoScrollState()
    val modelProducer = remember { CartesianChartModelProducer() }
    val maxDuration = data.values.max()
    LaunchedEffect(data) {
        modelProducer.runTransaction {
            columnSeries {
                series(
                    x = data.keys,
                    y = data.values.map { it.inWholeMilliseconds },
                )
            }
        }
    }

    val marker =
        DefaultCartesianMarker(
            label = rememberTextComponent(),
            valueFormatter = { _, targets ->
                val duration =
                    TimeUtils.getDurationString(
                        (targets.first() as ColumnCartesianLayerMarkerTarget).columns.first().entry.y.toDuration(
                            DurationUnit.MILLISECONDS,
                        ),
                    )
                duration
            },
        )

    CartesianChartHost(
        chart =
            rememberCartesianChart(
                rememberColumnCartesianLayer(),
                startAxis =
                    VerticalAxis.rememberStart(
                        valueFormatter = { _, y, _ ->
                            TimeUtils.getShortDurationString(y.toDuration(DurationUnit.MILLISECONDS), maxDuration)
                        },
                    ),
                bottomAxis =
                    HorizontalAxis.rememberBottom(
                        valueFormatter = { _, x, _ ->
                            xLables(x.toInt())
                        },
                    ),
                marker = marker,
            ),
        modelProducer = modelProducer,
        modifier = modifier,
        scrollState = scrollState,
    )
}

enum class PeriodType(val label: String) {
    DAY("天"),
    WEEK("周"),
    MONTH("月"),
    YEAR("年"),
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

            PeriodType.YEAR -> {
                val start = TimeUtils.getFirstDayOfYear(if (endDate > today) today else endDate)
                val end = TimeUtils.getLastDayOfYear(start)
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

                PeriodType.YEAR -> {
                    val start = startDate.minus(1, DateTimeUnit.YEAR)
                    copy(startDate = start, endDate = TimeUtils.getLastDayOfYear(start))
                }

                PeriodType.ALL_TIME -> this
            }

    val next: Period
        get() {
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

                PeriodType.YEAR -> {
                    val start = startDate.plus(1, DateTimeUnit.YEAR)
                    val end = TimeUtils.getLastDayOfYear(start)
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
                PeriodType.YEAR -> TimeUtils.getToday() > endDate
                PeriodType.ALL_TIME -> false
            }

    fun getString(): String =
        when (type) {
            PeriodType.DAY -> TimeUtils.getDisplayDate(startDate)
            PeriodType.WEEK -> TimeUtils.getDisplayPeriod(startDate, endDate)
            PeriodType.MONTH -> TimeUtils.getMonthString(startDate)
            PeriodType.YEAR -> startDate.year.toString()
            PeriodType.ALL_TIME -> "全部"
        }

    fun getDays(): List<LocalDate> {
        val days = mutableListOf<LocalDate>()
        var date = startDate
        while (date <= endDate) {
            days.add(date)
            date = date.plus(1, DateTimeUnit.DAY)
        }
        return days
    }
}
