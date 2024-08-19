package com.mean.traclock.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.mean.traclock.data.Record
import com.mean.traclock.ui.Constants.HORIZONTAL_MARGIN
import com.mean.traclock.ui.components.NoData
import com.mean.traclock.ui.navigation.HomeRoute
import com.mean.traclock.utils.TimeUtils
import com.mean.traclock.viewmodels.MainViewModel
import io.github.koalaplot.core.pie.DefaultSlice
import io.github.koalaplot.core.pie.PieChart
import io.github.koalaplot.core.util.ExperimentalKoalaPlotApi
import kotlinx.coroutines.flow.Flow
import org.jetbrains.compose.resources.stringResource
import traclock.composeapp.generated.resources.Res
import traclock.composeapp.generated.resources.no_record
import traclock.composeapp.generated.resources.records_duration
import ui.components.HomeScaffold

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

@OptIn(ExperimentalKoalaPlotApi::class)
@Composable
private fun Content(
    viewModel: MainViewModel,
    projectsTimeFlow: Flow<List<Record>>,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
) {
    val projectsTime by projectsTimeFlow.collectAsState(listOf())
    val data = projectsTime.map { (it.endTime - it.startTime).toFloat() }
    val duration = projectsTime.sumOf { it.endTime - it.startTime } / 1000
    val selected = remember { mutableIntStateOf(-1) }
    if (duration > 0) {
        Column(
            modifier
                .padding(contentPadding)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                Modifier
                    .padding(horizontal = HORIZONTAL_MARGIN)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(stringResource(Res.string.records_duration))
                Text(
                    TimeUtils.getDurationString(duration),
                    fontFamily = FontFamily.Monospace,
                )
            }
            PieChart(
                modifier = Modifier.padding(horizontal = HORIZONTAL_MARGIN),
                values = data,
                label = {
                    Text(viewModel.projects[projectsTime[it].project]?.name ?: "")
                },
                slice = {
                    DefaultSlice(color = Color(viewModel.projects[projectsTime[it].project]?.color ?: 0))
                },
                holeSize = 0.75F,
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
                        fontFamily = FontFamily.Monospace,
                        color = color,
                    )
                }
            }
        }
    } else {
        NoData(stringResource(Res.string.no_record), modifier = modifier.padding(contentPadding))
    }
}
