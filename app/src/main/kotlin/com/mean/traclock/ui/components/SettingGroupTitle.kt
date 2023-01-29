package com.mean.traclock.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mean.traclock.utils.Constants.HORIZONTAL_MARGIN

private val TOP_MARGIN = 24.dp
private val BOTTOM_MARGIN = 8.dp

@Composable
fun SettingGroupTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier
            .padding(horizontal = HORIZONTAL_MARGIN)
            .padding(start = 40.dp, top = 28.dp, bottom = 12.dp)
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
            .padding(start = 16.dp)
            .padding(top = TOP_MARGIN, bottom = BOTTOM_MARGIN)
    )
}
