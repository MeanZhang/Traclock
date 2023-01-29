package com.mean.traclock.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingItem(
    title: String,
    icon: ImageVector? = null,
    description: String? = null,
    onClick: () -> Unit = {}
) {
    ListItem(
        headlineText = { Text(title) },
        supportingText = { description?.let { Text(description) } },
        leadingContent = {
            icon?.let { Icon(it, contentDescription = title) }
        },
        modifier = Modifier.clickable { onClick() }
    )
}
