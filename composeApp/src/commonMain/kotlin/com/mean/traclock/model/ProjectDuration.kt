package com.mean.traclock.model

import androidx.compose.ui.graphics.Color
import androidx.room.Embedded
import kotlin.time.Duration

data class ProjectDuration(
    @Embedded val project: Project,
    val duration: Duration,
) {
    val id: Long
        get() = project.id
    val name: String
        get() = project.name
    val color: Color
        get() = project.color
}
