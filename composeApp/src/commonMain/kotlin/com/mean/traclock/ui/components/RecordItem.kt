package com.mean.traclock.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.mean.traclock.data.Record
import com.mean.traclock.utils.TimeUtils
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import traclock.composeapp.generated.resources.Res
import traclock.composeapp.generated.resources.delete
import ui.components.SmallOutlinedButton

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RecordItem(
    record: Record,
    projectName: String,
    color: Color,
    navToProject: (Long) -> Unit,
    modifier: Modifier = Modifier,
    detailView: Boolean = true,
    listState: LazyListState? = null,
    deleteRecord: (Record) -> Unit,
    startTiming: (Long) -> Unit,
    navToEditRecord: (Long) -> Unit,
) {
    val startTime = TimeUtils.getTimeString(record.startTime)
    val endTime = TimeUtils.getTimeString(record.endTime)
    var showMenu by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    ListItem(
        modifier =
            modifier
                .combinedClickable(
                    onClick = {
                        if (detailView) {
                            navToEditRecord(record.id)
                        } else {
                            navToProject(record.project)
                        }
                    },
                    onLongClick =
                        if (detailView) {
                            { showMenu = true }
                        } else {
                            null
                        },
                ),
        leadingContent = {
            Icon(
                imageVector = Icons.Default.Circle,
                contentDescription = null,
                tint = color,
                modifier =
                    Modifier
                        .size(20.dp),
            )
        },
        headlineContent = {
            Box {
                Text(projectName, maxLines = 1, overflow = TextOverflow.Ellipsis)
                DropdownMenu(showMenu, { showMenu = false }) {
                    DropdownMenuItem(
                        text = { Text(stringResource(Res.string.delete)) },
                        onClick = {
                            showMenu = false
                            deleteRecord(record)
                        },
                    )
                }
            }
        },
        supportingContent = {
            Text(
                if (detailView) {
                    "$startTime - $endTime"
                } else {
                    TimeUtils.getDurationString(record.startTime, record.endTime)
                },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        },
        trailingContent = {
            SmallOutlinedButton(
                text = TimeUtils.getDurationString(record.startTime, record.endTime),
            ) {
                scope.launch {
                    startTiming(record.project)
                    listState?.animateScrollToItem(0)
                }
            }
        },
    )
}
