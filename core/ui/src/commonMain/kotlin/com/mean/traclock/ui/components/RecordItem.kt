package com.mean.traclock.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.mean.traclock.CommonRes
import com.mean.traclock.Res
import com.mean.traclock.model.Record
import com.mean.traclock.ui.utils.onClick
import com.mean.traclock.utils.TimeUtils
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.coroutines.launch

@Composable
fun RecordItem(
    record: Record,
    projectName: String,
    color: Color,
    modifier: Modifier = Modifier,
    listState: LazyListState? = null,
    deleteRecord: (Record) -> Unit,
    startTiming:( (Long) -> Unit)?=null,
    navToEditRecord: (Long) -> Unit,
) {
    val startTime = TimeUtils.getTimeString(record.startTime)
    val endTime = TimeUtils.getTimeString(record.endTime)
    var showMenu by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    ListItem(
        modifier =
            modifier
                .onClick(
                    onClick = {
                        navToEditRecord(record.id)
                    },
                    onSecondaryClick =
                        { showMenu = true },
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
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.error,
                            )
                        },
                        text = { Text(stringResource(CommonRes.strings.delete), color = MaterialTheme.colorScheme.error) },
                        onClick = {
                            showMenu = false
                            deleteRecord(record)
                        },
                    )
                }
            }
        },
        supportingContent = {
            val durStr by remember {
                mutableStateOf(TimeUtils.getDurationString(record.startTime, record.endTime))
            }
            Text(
                "$startTime - $endTime ($durStr)",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        },
        trailingContent = {
            startTiming?.let {
                IconButton(onClick = {
                    scope.launch {
                        startTiming(record.projectId)
                        listState?.animateScrollToItem(0)
                    }
                }) {
                    Icon(
                        imageVector = Icons.Outlined.Timer,
                        contentDescription = stringResource(CommonRes.strings.start),
                        tint = MaterialTheme.colorScheme.outline,
                    )
                }
            }
        },
    )
}
