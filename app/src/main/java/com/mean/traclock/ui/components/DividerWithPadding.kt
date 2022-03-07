package com.mean.traclock.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mean.traclock.App

@Composable
fun DividerWithPadding() =
    Divider(Modifier.padding(horizontal = App.horizontalMargin))
