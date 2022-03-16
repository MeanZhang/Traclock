package com.mean.traclock.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mean.traclock.utils.HORIZONTAL_MARGIN

private enum class ProjectColor(val color: Color) {
    Blue(Color.Blue),
    Brown(Color(0xFF795548)),
    Green(Color.Green),
    Indigo(Color(0xFF3F51B5)),
    Orange(Color(0xFFE65100)),
    Pink(Color(0xFFE91E63)),
    Purple(Color(0xFF6200EE)),
    Red(Color.Red),
    Teal(Color(0xFF03DAC6)),
    Yellow(Color.Yellow)
}

@OptIn(
    ExperimentalMaterial3Api::class,
)
@Composable
fun ColorPicker(
    onColorSelected: (Color) -> Unit
) {
    var selected by remember { mutableStateOf(ProjectColor.Blue) }
    LazyVerticalGrid(
        columns = GridCells.Adaptive(60.dp),
        contentPadding = PaddingValues(horizontal = HORIZONTAL_MARGIN, vertical = 16.dp)
    ) {
        items(ProjectColor.values()) { color ->
            RadioButton(
                selected = selected == color,
                onClick = {
                    onColorSelected(color.color)
                    selected = color
                },
                colors = RadioButtonDefaults.colors(
                    selectedColor = color.color,
                    unselectedColor = color.color
                )
            )
        }
    }
}
