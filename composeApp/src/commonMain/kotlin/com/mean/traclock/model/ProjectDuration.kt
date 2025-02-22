package com.mean.traclock.model

import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

data class ProjectDurationEntry(
    val projectId: Long,
    val duration: Long,
) {
    fun toProjectDuration(): ProjectDuration {
        return ProjectDuration(projectId, duration.toDuration(DurationUnit.MILLISECONDS))
    }
}

data class ProjectDuration(
    val projectId: Long,
    val duration: Duration,
)
