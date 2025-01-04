package com.mean.traclock.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Keyboard
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.mean.timepicker.TimeInput
import com.mean.timepicker.TimePicker
import com.mean.timepicker.TimePickerDialog
import com.mean.timepicker.rememberTimePickerState
import com.mean.traclock.utils.TimeUtils
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import org.jetbrains.compose.resources.stringResource
import traclock.composeapp.generated.resources.Res
import traclock.composeapp.generated.resources.cancel
import traclock.composeapp.generated.resources.ok
import traclock.composeapp.generated.resources.time_picker_enter_time
import traclock.composeapp.generated.resources.time_picker_select_time
import traclock.composeapp.generated.resources.time_picker_switch_to_text_input
import traclock.composeapp.generated.resources.time_picker_switch_to_touch_input

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateTimePicker(
    modifier: Modifier = Modifier,
    initailTimeMillis: Long,
    onDateChoose: (LocalDate) -> Unit,
    onTimeChoose: (LocalTime) -> Unit,
) {
    val textColor = MaterialTheme.colorScheme.primary
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    SingleChoiceSegmentedButtonRow(modifier = modifier) {
        SegmentedButton(
            shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2),
            onClick = { showDatePicker = true },
            selected = false,
            colors =
                SegmentedButtonDefaults.colors(
                    inactiveContentColor = textColor,
                ),
        ) {
            Text(TimeUtils.getDateStringWithYear(initailTimeMillis))
        }
        SegmentedButton(
            shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2),
            onClick = { showTimePicker = true },
            selected = false,
            colors =
                SegmentedButtonDefaults.colors(
                    inactiveContentColor = textColor,
                ),
        ) {
            Text(TimeUtils.getTimeStringWithSecond(initailTimeMillis))
        }
    }
    if (showDatePicker) {
        val datePickerState =
            rememberDatePickerState(initialSelectedDateMillis = TimeUtils.getUtcDateMillis(initailTimeMillis))
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { onDateChoose(TimeUtils.utcMillisToLocalDate(it)) }
                    showDatePicker = false
                }) {
                    Text(stringResource(Res.string.ok))
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDatePicker = false
                }) {
                    Text(stringResource(Res.string.cancel))
                }
            },
        ) {
            DatePicker(datePickerState)
        }
    }
    if (showTimePicker) {
        val timePickerState = rememberTimePickerState(initailTimeMillis)
        var showingPicker by remember { mutableStateOf(true) }
        TimePickerDialog(
            title =
                if (showingPicker) {
                    stringResource(Res.string.time_picker_select_time)
                } else {
                    stringResource(Res.string.time_picker_enter_time)
                },
            onCancel = { showTimePicker = false },
            onConfirm = {
                onTimeChoose(timePickerState.selectedTime)
                showTimePicker = false
            },
            toggle = {
                IconButton(onClick = { showingPicker = !showingPicker }) {
                    val icon =
                        if (showingPicker) {
                            Icons.Outlined.Keyboard
                        } else {
                            Icons.Outlined.Schedule
                        }
                    Icon(
                        icon,
                        contentDescription =
                            if (showingPicker) {
                                stringResource(Res.string.time_picker_switch_to_text_input)
                            } else {
                                stringResource(Res.string.time_picker_switch_to_touch_input)
                            },
                    )
                }
            },
        ) {
            if (showingPicker) {
                TimePicker(state = timePickerState)
            } else {
                TimeInput(state = timePickerState)
            }
        }
    }
}
