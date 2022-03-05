package com.mean.traclock.ui.screens

import android.graphics.Color
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.mean.traclock.App
import com.mean.traclock.R
import com.mean.traclock.database.AppDatabase
import com.mean.traclock.database.Record
import com.mean.traclock.ui.components.charts.ChartData
import com.mean.traclock.ui.components.charts.PieChart
import com.mean.traclock.util.getDurationString
import com.mean.traclock.util.getIntDate

@Composable
fun Statistics() {
    val date = getIntDate(System.currentTimeMillis())
    val projectsTime by AppDatabase.getDatabase(App.context).recordDao().getProjectsTimeOfDate(date)
        .collectAsState(listOf())
    Column {
        Row(
            Modifier
                .padding(horizontal = App.horizontalMargin)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val duration = projectsTime.sumOf { (it.endTime - it.startTime) / 1000 }
            Text(stringResource(R.string.records_duration))
            Text(getDurationString(duration))
        }
        PieChart(
            getPieData(projectsTime),
            Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .padding(horizontal = App.horizontalMargin)
        )
    }
}

fun getPieData(projectsTime: List<Record>) =
    projectsTime.map {
        ChartData(
            it.project,
            it.endTime - it.startTime,
            App.projectsList[it.project] ?: Color.CYAN
        )
    }