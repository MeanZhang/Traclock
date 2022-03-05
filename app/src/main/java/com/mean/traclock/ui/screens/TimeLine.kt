package com.mean.traclock.ui.screens

import android.content.Context
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mean.traclock.App
import com.mean.traclock.database.Record
import com.mean.traclock.database.TimeByDate
import com.mean.traclock.ui.components.DateTitle
import com.mean.traclock.ui.components.DividerWithPadding
import com.mean.traclock.ui.components.RecordItem
import com.mean.traclock.ui.components.TimingCard
import com.mean.traclock.util.TimingControl
import com.mean.traclock.util.getDataString
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TimeLine(
    context: Context,
    recordsFlow: Flow<List<Record>>,
    projectTime: Flow<List<TimeByDate>>,
    detailView: Boolean,
//    listState: LazyListState,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    val records by recordsFlow.collectAsState(listOf())
    val time by projectTime.collectAsState(listOf())
    val isTiming by App.isTiming.collectAsState(false)
    if (!detailView) {
        Log.d("Boost-Mean", records.toString())
    }
    LazyColumn(
//        state = listState,
        contentPadding = contentPadding
    ) {
        item {
            TimingCard(
                TimingControl.getProjectName(),
                TimingControl.getStartTime(),
                isTiming
            )
        }

        items(records.size) { i ->
            val record = records[i]

            val color = Color(App.projectsList[record.project] ?: 0)
            if (i == 0 || (i > 0 && record.date != records[i - 1].date)) {
                DateTitle(
                    date = getDataString(record.date),
                    duration = time.find { it.date == record.date }?.time ?: 0L
                )
                DividerWithPadding()
            }
            RecordItem(context, record, color, detailView)
            DividerWithPadding()
        }
    }
}