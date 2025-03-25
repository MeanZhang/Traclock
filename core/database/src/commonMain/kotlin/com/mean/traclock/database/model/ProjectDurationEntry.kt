package com.mean.traclock.database.model

import androidx.compose.ui.graphics.Color
import androidx.room.Embedded
import com.mean.traclock.model.ProjectDuration
import kotlin.time.Duration

data class ProjectDurationEntry(
    @Embedded val project: ProjectEntry,
    val duration: Duration,
) {
    val id: Long
        get() = project.id
    val name: String
        get() = project.name
    val color: Color
        get() = project.color
}

fun ProjectDurationEntry.asExternalProjectDuration() =
    ProjectDuration(
        project = project.asExternalProject(),
        duration = duration,
    )
