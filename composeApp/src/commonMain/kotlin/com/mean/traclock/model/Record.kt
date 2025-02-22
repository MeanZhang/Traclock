package com.mean.traclock.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

/**
 * @param projectId 项目 ID
 * @param startTime 开始时刻
 * @param endTime 结束时刻
 * @param date 开始日期
 * @param recordId 记录的 ID
 */
@Entity
data class Record(
    var projectId: Long,
    var startTime: Instant,
    var endTime: Instant,
    var date: LocalDate = startTime.toLocalDateTime(TimeZone.currentSystemDefault()).date,
    @PrimaryKey(autoGenerate = true)
    var recordId: Long = 0,
)
