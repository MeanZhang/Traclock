package com.mean.traclock.ui.components

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.mean.traclock.R
import com.mean.traclock.data.DataModel
import com.mean.traclock.ui.Constants.HORIZONTAL_MARGIN
import com.mean.traclock.utils.TimeUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun TimingCard(
    project: String,
    startTime: Long,
    isTiming: Boolean,
) {
    AnimatedVisibility(
        visible = isTiming,
        enter = expandVertically(),
        exit = shrinkVertically(),
    ) {
        var now by remember {
            mutableStateOf(System.currentTimeMillis())
        }
        val scope = rememberCoroutineScope()
        if (isTiming) {
            scope.launch {
                while (isTiming) {
                    now = System.currentTimeMillis()
                    delay(1000)
                }
            }
        }
        Card(
            modifier = Modifier
                .padding(vertical = 16.dp, horizontal = HORIZONTAL_MARGIN)
                .fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
            shape = RoundedCornerShape(32.dp),
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 24.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.AccessTime,
                        contentDescription = stringResource(R.string.tracking),
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .size(30.dp),
                    )
                    Column {
                        Text(project, style = MaterialTheme.typography.titleLarge)
                        Text(
                            TimeUtils.getDurationString(startTime, now),
                            style = MaterialTheme.typography.bodyMedium,
                            fontFamily = FontFamily.Monospace,
                        )
                    }
                }
                IconButton(onClick = {
                    scope.launch {
                        DataModel.dataModel.stopTimer()
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
}
