package com.mean.traclock.ui.components

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.mean.traclock.R
import com.mean.traclock.data.DataModel
import com.mean.traclock.database.Record
import com.mean.traclock.ui.Constants.HORIZONTAL_MARGIN
import com.mean.traclock.ui.EditRecordActivity
import com.mean.traclock.ui.ProjectActivity
import com.mean.traclock.utils.TimeUtils
import kotlinx.coroutines.launch

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalFoundationApi::class, ExperimentalPermissionsApi::class)
@Composable
fun RecordItem(
    record: Record,
    color: Color,
    detailView: Boolean = true,
    listState: LazyListState? = null,
) {
    val startTime = TimeUtils.getTimeString(record.startTime)
    val endTime = TimeUtils.getTimeString(record.endTime)
    val projectName = record.project
    var showMenu by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val notificationPermissionState =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            rememberPermissionState(android.Manifest.permission.POST_NOTIFICATIONS)
        } else {
            null
        }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = {
                    val activity =
                        if (detailView) EditRecordActivity::class.java else ProjectActivity::class.java
                    val intent = Intent(context, activity)
                    if (detailView) {
                        putRecord(intent, record)
                    } else {
                        intent.putExtra("projectName", record.project)
                    }
                    context.startActivity(intent)
                },
                onLongClick = if (detailView) {
                    { showMenu = true }
                } else {
                    null
                },
            )
            .padding(vertical = 12.dp, horizontal = HORIZONTAL_MARGIN),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Circle,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .size(20.dp),
                )
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(projectName)
                    Text(
                        if (detailView) {
                            "$startTime - $endTime"
                        } else {
                            TimeUtils.getDurationString(record.startTime, record.endTime, false)
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
            SmallOutlinedButton(
                text = TimeUtils.getDurationString(record.startTime, record.endTime, detailView),
            ) {
                scope.launch {
                    notificationPermissionState?.launchPermissionRequest()
                    DataModel.dataModel.startTimer(record.project)
                    listState?.animateScrollToItem(0)
                }
            }
        }
        DropdownMenu(showMenu, { showMenu = false }) {
            DropdownMenuItem(
                text = { Text(stringResource(R.string.delete)) },
                onClick = { showMenu = false; DataModel.dataModel.deleteRecord(record) },
            )
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

@Preview
@Composable
fun RecordItemPreview() {
    RecordItem(
        record = Record(
            project = "Project",
            startTime = 0,
            endTime = 0,
            date = 0,
        ),
        color = Color.Red,
    )
}
