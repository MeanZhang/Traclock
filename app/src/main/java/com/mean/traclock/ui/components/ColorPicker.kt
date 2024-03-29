package com.mean.traclock.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mean.traclock.ui.Constants.HORIZONTAL_MARGIN
import com.mean.traclock.ui.ProjectColor

@Composable
fun ColorPicker(
    color: Color,
    modifier: Modifier = Modifier,
    onColorSelected: (Color) -> Unit,
) {
    val defaultColor =
        ProjectColor.entries.find { it.color == color } ?: ProjectColor.entries.toTypedArray()[0]
    var selected by remember { mutableStateOf(defaultColor) }
    LazyVerticalGrid(
        columns = GridCells.Adaptive(60.dp),
        contentPadding = PaddingValues(horizontal = HORIZONTAL_MARGIN, vertical = 16.dp),
        modifier = modifier,
    ) {
        items(ProjectColor.entries.toTypedArray()) { color ->
            RadioButton(
                selected = selected == color,
                onClick = {
                    onColorSelected(color.color)
                    selected = color
                },
                colors =
                    RadioButtonDefaults.colors(
                        selectedColor = color.color,
                        unselectedColor = color.color,
                    ),
            )
        }
    }
}
