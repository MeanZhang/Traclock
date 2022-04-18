package com.mean.traclock.ui.components

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mean.traclock.R
import com.mean.traclock.utils.Config.HORIZONTAL_MARGIN
import com.mean.traclock.utils.TimingControl
import com.mean.traclock.utils.getDurationString
import com.mean.traclock.utils.getTimeWithSeconds
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun TimingCard(
    project: String,
    startTime: Long,
    isTiming: Boolean
) {
    AnimatedVisibility(
        visible = isTiming,
        enter = expandVertically(),
        exit = shrinkVertically()
    ) {
        val begin = getTimeWithSeconds(startTime)
        var now by remember {
            mutableStateOf(System.currentTimeMillis())
        }
        val scope = rememberCoroutineScope()
        scope.launch {
            while (isTiming) {
                now = System.currentTimeMillis()
                delay(1000)
            }
        }
        Column(modifier = Modifier.padding(horizontal = HORIZONTAL_MARGIN)) {
            // 记录中
            Text(
                stringResource(R.string.tracking),
                style = MaterialTheme.typography.headlineMedium
            )
            Card(
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // 项目名
                        Text(project, style = MaterialTheme.typography.headlineSmall)
                        // 持续时间
                        Text(
                            getDurationString(startTime, now),
                            style = MaterialTheme.typography.headlineMedium,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // 开始时间
                        Text(
                            begin,
                            style = MaterialTheme.typography.headlineSmall
                        )
                        // 停止按钮
                        TextButton(onClick = {
                            scope.cancel()
                            TimingControl.stopRecord()
                        }) {
                            Text(
                                text = stringResource(R.string.stop),
                                style = MaterialTheme.typography.headlineSmall,
                                textAlign = TextAlign.Right
                            )
                        }
                    }
                }
            }
        }
    }
}
