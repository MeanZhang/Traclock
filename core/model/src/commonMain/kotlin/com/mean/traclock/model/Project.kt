package com.mean.traclock.model

import androidx.compose.ui.graphics.Color

data class Project(
    val name: String,
    val color: Color,
    val id: Long = 0,
)
