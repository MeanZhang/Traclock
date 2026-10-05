package com.mean.traclock.model

import androidx.compose.ui.graphics.Color
import kotlin.time.Duration

data class ProjectDuration(
    val project: Project,
    val duration: Duration,
) {
    val id: Long
        get() = project.id
    val name: String
        get() = project.name
    val color: Color
        get() = project.color
}
