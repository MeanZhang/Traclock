package com.mean.traclock.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mean.traclock.util.HORIZONTAL_MARGIN
import com.mean.traclock.util.getDurationString

@Composable
fun DateTitle(date: String, duration: Long) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = HORIZONTAL_MARGIN),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            date,
            style = MaterialTheme.typography.headlineSmall
        )
        Text(
            getDurationString(duration),
            style = MaterialTheme.typography.headlineSmall
        )
    }
}
