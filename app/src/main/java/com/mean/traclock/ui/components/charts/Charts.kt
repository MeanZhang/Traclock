package com.mean.traclock.ui.components.charts

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.mean.traclock.util.log

@Composable
fun LineChart(lineData: LineData) {
    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        factory = { context ->
            LineChart(context).apply {
                this.data = lineData
            }
        })
}

@Composable
fun PieChart(pieData: List<ChartData>, modifier: Modifier = Modifier) {
    log(pieData.toString())
    AndroidView(
        modifier = modifier,
        factory = { context ->
            PieChart(context).apply {
                val list = pieData.map { PieEntry(it.time.toFloat(), it.name) }
                val dataset = PieDataSet(list, "test")
                dataset.colors = pieData.map { it.color }
                this.data = PieData(dataset)
            }
        },
        update = {
            val list = pieData.map { PieEntry(it.time.toFloat(), it.name) }
            val dataset = PieDataSet(list, "test")
            dataset.colors = pieData.map { it.color }
            it.data = PieData(dataset)
        })
}