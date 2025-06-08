package com.mean.traclock.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.mean.traclock.CommonRes
import com.mean.traclock.model.ProjectDuration
import com.mean.traclock.utils.TimeUtils
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.coroutines.launch

@Composable
fun ProjectDurationItem(
    projectDuration: ProjectDuration,
    projectName: String,
    color: Color,
    navToProject: (Long) -> Unit,
    modifier: Modifier = Modifier,
    listState: LazyListState? = null,
    startTiming: (Long) -> Unit,
) {
    val scope = rememberCoroutineScope()
    ListItem(
        modifier = modifier.clickable { navToProject(projectDuration.id) },
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
        headlineContent = { Text(projectName, maxLines = 1, overflow = TextOverflow.Ellipsis) },
        supportingContent = {
            Text(
                TimeUtils.getDurationString(projectDuration.duration),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        },
        trailingContent = {
            IconButton(onClick = {
                scope.launch {
                    scope.launch {
                        startTiming(projectDuration.id)
                        listState?.animateScrollToItem(0)
                    }
                }
            }) {
                Icon(
                    imageVector = Icons.Outlined.Timer,
                    contentDescription = stringResource(CommonRes.strings.start),
                    tint = MaterialTheme.colorScheme.outline,
                )
            }
        },
    )
}
