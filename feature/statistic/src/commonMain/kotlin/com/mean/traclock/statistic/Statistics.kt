package com.mean.traclock.statistic

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
import com.mean.traclock.designsystem.Constants.HORIZONTAL_MARGIN
import com.mean.traclock.model.RecordWithProject
import com.mean.traclock.statistic.model.Period
import com.mean.traclock.statistic.model.PeriodType
import com.mean.traclock.ui.HomeRoute
import com.mean.traclock.ui.components.HomeScaffold
import com.mean.traclock.utils.TimeUtils
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.compose.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.compose.cartesian.data.columnModel
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.marker.ColumnCartesianLayerMarkerTarget
import com.patrykandpatrick.vico.compose.cartesian.marker.DefaultCartesianMarker
import com.patrykandpatrick.vico.compose.cartesian.marker.rememberDefaultCartesianMarker
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoScrollState
import com.patrykandpatrick.vico.compose.common.Fill
import com.patrykandpatrick.vico.compose.common.component.rememberTextComponent
import com.patrykandpatrick.vico.compose.pie.PieChart
import com.patrykandpatrick.vico.compose.pie.PieChartHost
import com.patrykandpatrick.vico.compose.pie.PieSize
import com.patrykandpatrick.vico.compose.pie.data.PieChartModelProducer
import com.patrykandpatrick.vico.compose.pie.data.pieModel
import com.patrykandpatrick.vico.compose.pie.rememberPieChart
import kotlinx.datetime.LocalDate
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import org.koin.compose.viewmodel.koinViewModel
import kotlin.text.get
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@Composable
fun Statistics(
    viewModel: StatisticViewModel = koinViewModel(),
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

@OptIn(ExperimentalMaterial3Api::class)
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
    val pieModelProducer = remember { PieChartModelProducer() }
    LaunchedEffect(projectsDuration) {
        if (data.isNotEmpty()) {
            pieModelProducer.runTransaction {
                pieModel { series(data) }
            }
        }
    }
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
                PieChartHost(
                    chart =
                        rememberPieChart(
                            sliceProvider =
                                PieChart.SliceProvider.series(
                                    projectsDuration.map { PieChart.Slice(fill = Fill(it.color)) },
                                ),
                            spacing = 2.dp,
                            innerSize = PieSize.Inner.fixed(96.dp),
                        ),
                    modelProducer = pieModelProducer,
                    modifier =
                        Modifier
                            .height(200.dp)
                            .padding(8.dp),
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
            it.dayOfWeek.isoDayNumber to (records[it] ?: Duration.ZERO)
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
            columnModel {
                series(
                    x = data.keys,
                    y = data.values.map { it.inWholeMilliseconds },
                )
            }
        }
    }

    val marker =
        rememberDefaultCartesianMarker(
            label = rememberTextComponent(),
            valueFormatter =
                DefaultCartesianMarker.ValueFormatter { _, targets ->
                    TimeUtils.getDurationString(
                        (targets.first() as ColumnCartesianLayerMarkerTarget).columns.first().entry.y.toDuration(
                            DurationUnit.MILLISECONDS,
                        ),
                    )
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
