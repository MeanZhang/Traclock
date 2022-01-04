package com.mean.traclock.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mean.traclock.TraclockApplication

@Composable
fun DividerWithPadding() = Divider(
    color = MaterialTheme.colorScheme.inverseOnSurface,
    modifier = Modifier.padding(horizontal = TraclockApplication.horizontalMargin)
)