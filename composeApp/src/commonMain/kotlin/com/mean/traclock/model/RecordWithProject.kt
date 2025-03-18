package com.mean.traclock.model

import androidx.compose.ui.graphics.Color
import androidx.room.Embedded
import kotlinx.datetime.Instant

data class RecordWithProject(
    @Embedded val record: Record,
    @Embedded val project: Project,
) {
    val projectId
        get() = project.id
    val startTime: Instant
        get() = record.startTime
    val endTime: Instant
        get() = record.endTime
    val projectName: String
        get() = project.name
    val color: Color
        get() = project.color
}
