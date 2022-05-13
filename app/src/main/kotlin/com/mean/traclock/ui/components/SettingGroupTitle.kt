package com.mean.traclock.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mean.traclock.utils.Config.HORIZONTAL_MARGIN

@Composable
fun SettingGroupTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier
            .padding(horizontal = HORIZONTAL_MARGIN)
            .padding(start = 52.dp, top = 28.dp, bottom = 12.dp)
    )
}

@Composable
fun SettingGroupTitleWithoutIcon(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier
            .padding(
                horizontal = HORIZONTAL_MARGIN
            )
            .padding(top = 28.dp, bottom = 12.dp)
    )
}
