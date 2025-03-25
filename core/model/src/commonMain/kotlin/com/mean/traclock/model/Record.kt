package com.mean.traclock.model

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

data class Record(
    val projectId: Long,
    val startTime: Instant,
    val endTime: Instant,
    val date: LocalDate = startTime.toLocalDateTime(TimeZone.currentSystemDefault()).date,
    val id: Long = 0,
)
