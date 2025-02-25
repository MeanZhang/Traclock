package com.mean.traclock.ui.utils

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalFoundationApi::class)
@Composable
actual fun Modifier.onClick(
    onClick: () -> Unit,
    onSecondaryClick: () -> Unit,
): Modifier {
    return this.combinedClickable(
        onClick = onClick,
        onLongClick = onSecondaryClick,
    )
}
