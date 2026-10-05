package com.mean.traclock.designsystem.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun Modifier.onClick(
    onClick: () -> Unit,
    onSecondaryClick: () -> Unit,
): Modifier
