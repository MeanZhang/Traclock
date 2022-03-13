package com.mean.traclock.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mean.traclock.utils.HORIZONTAL_MARGIN

@Composable
fun SettingGroupTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier
            .padding(vertical = HORIZONTAL_MARGIN, horizontal = 16.dp)
            .padding(start = 52.dp)
    )
}

@Composable
fun SettingGroupTitleWithoutIcon(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(
            horizontal = HORIZONTAL_MARGIN,
            vertical = 12.dp
        )
    )
}
