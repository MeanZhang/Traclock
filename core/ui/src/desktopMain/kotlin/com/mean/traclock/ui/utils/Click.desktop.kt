package com.mean.traclock.ui.utils

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.PointerMatcher
import androidx.compose.foundation.clickable
import androidx.compose.foundation.onClick
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerButton

@OptIn(ExperimentalFoundationApi::class)
@Composable
actual fun Modifier.onClick(
    onClick: () -> Unit,
    onSecondaryClick: () -> Unit,
): Modifier {
    return this.clickable { onClick() }.onClick(
        enabled = true,
        matcher = PointerMatcher.mouse(PointerButton.Secondary),
        onClick = onSecondaryClick,
    )
}
