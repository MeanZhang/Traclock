package com.mean.traclock.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.outlined.TimerOff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mean.traclock.R
import com.mean.traclock.ui.Constants.HORIZONTAL_MARGIN
import com.mean.traclock.utils.TimeUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun TimingCard(
    projectName: String,
    startTime: Long?,
    stopTiming: suspend () -> Unit,
    modifier: Modifier = Modifier,
) {
    var now by remember {
        mutableLongStateOf(System.currentTimeMillis())
    }
    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        scope.launch {
            while (true) {
                now = System.currentTimeMillis()
                delay(1000)
            }
        }
    }
    Card(
        modifier =
            modifier
                .padding(vertical = 16.dp, horizontal = HORIZONTAL_MARGIN)
                .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        shape = RoundedCornerShape(32.dp),
    ) {
        Row(
            modifier =
                Modifier
                    .padding(horizontal = 16.dp, vertical = 24.dp)
                    .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                Icons.Default.AccessTime,
                contentDescription = stringResource(R.string.tracking),
                modifier =
                    Modifier
                        .padding(end = 12.dp)
                        .size(30.dp),
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = projectName,
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    TimeUtils.getDurationString(startTime ?: System.currentTimeMillis(), now),
                    style = MaterialTheme.typography.bodyMedium,
                    fontFamily = FontFamily.Monospace,
                )
            }
            IconButton(onClick = {
                scope.launch {
                    stopTiming()
                }
            }) {
                Icon(
                    Icons.Outlined.TimerOff,
                    contentDescription = stringResource(R.string.stop),
                    modifier = Modifier.size(30.dp),
                )
            }
        }
    }
}

@Preview(name = "计时卡片")
@Composable
fun TimingCardPreview() {
    TimingCard(
        projectName = "测试",
        startTime = System.currentTimeMillis() - 10000,
        stopTiming = {},
    )
}

@Preview(name = "长名字计时卡片")
@Composable
fun LongNameTimingCardPreview() {
    TimingCard(
        projectName = "名字很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长的项目",
        startTime = System.currentTimeMillis() - 10000,
        stopTiming = {},
    )
}
