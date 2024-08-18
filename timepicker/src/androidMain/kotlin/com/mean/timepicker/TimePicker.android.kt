package com.mean.timepicker

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.platform.LocalConfiguration

@OptIn(ExperimentalMaterial3Api::class)
@ReadOnlyComposable
@Composable
internal actual fun defaultTimePickerLayoutType(): TimePickerLayoutType =
    with(LocalConfiguration.current) {
        if (screenHeightDp < screenWidthDp) {
            TimePickerLayoutType.Horizontal
        } else {
            TimePickerLayoutType.Vertical
        }
    }