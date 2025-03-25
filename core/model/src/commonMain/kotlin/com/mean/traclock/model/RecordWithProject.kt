package com.mean.traclock.model

import androidx.compose.ui.graphics.Color
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate

data class RecordWithProject(
    val record: Record,
    val project: Project,
) {
    val id: Long
        get() = record.id
    val projectId: Long
        get() = record.projectId
    val startTime: Instant
        get() = record.startTime
    val endTime: Instant
        get() = record.endTime
    val date: LocalDate
        get() = record.date
    val name: String
        get() = project.name
    val color: Color
        get() = project.color
}
