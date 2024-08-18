package com.mean.traclock.ui

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/** 常量 */
object Constants {
    /** 水平边距 */
    val HORIZONTAL_MARGIN = 16.dp
}

enum class ProjectColor(val color: Color) {
    Blue(Color.Blue),
    Brown(Color(0xFF795548)),
    Green(Color.Green),
    Indigo(Color(0xFF3F51B5)),
    Orange(Color(0xFFE65100)),
    Pink(Color(0xFFE91E63)),
    Purple(Color(0xFF6200EE)),
    Red(Color.Red),
    Teal(Color(0xFF03DAC6)),
    Yellow(Color.Yellow),
}
