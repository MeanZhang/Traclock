package com.mean.traclock.ui.components

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mean.traclock.R
import com.mean.traclock.App
import com.mean.traclock.util.*
import com.mean.traclock.util.TimingControl
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
            while (true) {
                now = System.currentTimeMillis()
                delay(1000)
            }
        }
        Column(modifier = Modifier.padding(horizontal = App.horizontalMargin)) {
            //记录中
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
                        //项目名
                        Text(project, style = MaterialTheme.typography.headlineSmall)
                        //持续时间
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
                        //开始时间
                        Text(
                            begin,
                            style = MaterialTheme.typography.headlineSmall
                        )
                        //停止按钮
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