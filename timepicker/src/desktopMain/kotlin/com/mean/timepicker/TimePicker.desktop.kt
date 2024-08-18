package com.mean.timepicker

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalWindowInfo

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@ReadOnlyComposable
@Composable
internal actual fun defaultTimePickerLayoutType(): TimePickerLayoutType  {
    return with(LocalWindowInfo.current) {
        if (containerSize.height < containerSize.width) {
            TimePickerLayoutType.Horizontal
        } else {
            TimePickerLayoutType.Vertical
        }
    }
}