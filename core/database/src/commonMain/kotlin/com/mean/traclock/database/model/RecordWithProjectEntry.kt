package com.mean.traclock.database.model

import androidx.compose.ui.graphics.Color
import androidx.room.Embedded
import com.mean.traclock.model.RecordWithProject
import kotlinx.datetime.Instant

data class RecordWithProjectEntry(
    @Embedded val record: RecordEntry,
    @Embedded val project: ProjectEntry,
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

fun RecordWithProjectEntry.asExternalRecordWithProject() =
    RecordWithProject(
        record = record.asExternalRecord(),
        project = project.asExternalProject(),
    )
