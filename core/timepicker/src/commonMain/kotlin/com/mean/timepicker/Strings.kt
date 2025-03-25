package com.mean.timepicker

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import com.mean.timepicker.resources.Res
import com.mean.timepicker.resources.close_drawer
import com.mean.timepicker.resources.close_sheet
import com.mean.timepicker.resources.m3c_time_picker_am
import com.mean.timepicker.resources.m3c_time_picker_hour
import com.mean.timepicker.resources.m3c_time_picker_hour_24h_suffix
import com.mean.timepicker.resources.m3c_time_picker_hour_selection
import com.mean.timepicker.resources.m3c_time_picker_hour_suffix
import com.mean.timepicker.resources.m3c_time_picker_hour_text_field
import com.mean.timepicker.resources.m3c_time_picker_minute
import com.mean.timepicker.resources.m3c_time_picker_minute_selection
import com.mean.timepicker.resources.m3c_time_picker_minute_suffix
import com.mean.timepicker.resources.m3c_time_picker_minute_text_field
import com.mean.timepicker.resources.m3c_time_picker_period_toggle_description
import com.mean.timepicker.resources.m3c_time_picker_pm
import com.mean.timepicker.resources.m3c_time_picker_second
import com.mean.timepicker.resources.m3c_time_picker_second_selection
import com.mean.timepicker.resources.m3c_time_picker_second_text_field
import com.mean.timepicker.resources.m3c_tooltip_pane_description
import com.mean.timepicker.resources.navigation_menu
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import kotlin.jvm.JvmInline

@Composable
internal fun getString(string: Strings): String {
    return stringResource(string.value)
}

@JvmInline
@Immutable
internal value class Strings(val value: StringResource) {
    companion object {
        inline val TimePickerAM
            get() = Strings(Res.string.m3c_time_picker_am)

        inline val TimePickerPM
            get() = Strings(Res.string.m3c_time_picker_pm)

        inline val TimePickerPeriodToggle
            get() = Strings(Res.string.m3c_time_picker_period_toggle_description)

        inline val TimePickerMinuteSelection
            get() = Strings(Res.string.m3c_time_picker_minute_selection)

        inline val TimePickerSecondSelection
            get() = Strings(Res.string.m3c_time_picker_second_selection)

        inline val TimePickerHourSelection
            get() = Strings(Res.string.m3c_time_picker_hour_selection)

        inline val TimePickerHourSuffix
            get() = Strings(Res.string.m3c_time_picker_hour_suffix)

        inline val TimePickerMinuteSuffix
            get() = Strings(Res.string.m3c_time_picker_minute_suffix)

        inline val TimePicker24HourSuffix
            get() = Strings(Res.string.m3c_time_picker_hour_24h_suffix)

        inline val TimePickerHour
            get() = Strings(Res.string.m3c_time_picker_hour)

        inline val TimePickerMinute
            get() = Strings(Res.string.m3c_time_picker_minute)

        inline val TimePickerSecond
            get() = Strings(Res.string.m3c_time_picker_second)

        inline val TimePickerHourTextField
            get() = Strings(Res.string.m3c_time_picker_hour_text_field)

        inline val TimePickerMinuteTextField
            get() = Strings(Res.string.m3c_time_picker_minute_text_field)

        inline val TimePickerSecondTextField
            get() = Strings(Res.string.m3c_time_picker_second_text_field)

        inline val TooltipPaneDescription
            get() = Strings(Res.string.m3c_tooltip_pane_description)

        inline val NavigationMenu
            get() = Strings(Res.string.navigation_menu)

        inline val CloseDrawer
            get() = Strings(Res.string.close_drawer)

        inline val CloseSheet
            get() = Strings(Res.string.close_sheet)
    }
}
