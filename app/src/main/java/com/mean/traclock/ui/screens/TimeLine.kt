package com.mean.traclock.ui.screens

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import com.mean.traclock.TraclockApplication
import com.mean.traclock.database.Record
import com.mean.traclock.database.TimeByDate
import com.mean.traclock.ui.components.*
import com.mean.traclock.util.TimingControl
import com.mean.traclock.util.getDataString

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TimeLine(
    context: Context,
    recordsLiveData: LiveData<List<Record>>,
    projectTime: LiveData<List<TimeByDate>>,
    detailView: Boolean,
//    listState: LazyListState,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    val records by recordsLiveData.observeAsState(listOf())
    val time by projectTime.observeAsState(listOf())
    val isTiming by TraclockApplication.isTiming.observeAsState(false)
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

            val color = Color(TraclockApplication.projectsList[record.project] ?: 0)
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

fun putRecord(intent: Intent, record: Record) {
    intent.putExtra("id", record.id)
    intent.putExtra("project", record.project)
    intent.putExtra("startTime", record.startTime)
    intent.putExtra("endTime", record.endTime)
    intent.putExtra("date", record.date)
}