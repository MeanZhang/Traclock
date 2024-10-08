package ui.components

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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.mean.traclock.ui.Constants.HORIZONTAL_MARGIN
import com.mean.traclock.utils.TimeUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import traclock.composeapp.generated.resources.Res
import traclock.composeapp.generated.resources.stop
import traclock.composeapp.generated.resources.tracking

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
                contentDescription = stringResource(Res.string.tracking),
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
                    contentDescription = stringResource(Res.string.stop),
                    modifier = Modifier.size(30.dp),
                )
            }
        }
    }
}
