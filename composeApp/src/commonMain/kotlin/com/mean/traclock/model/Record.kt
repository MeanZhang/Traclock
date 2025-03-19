package com.mean.traclock.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
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
 * @param id 记录的 ID
 */
@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Project::class,
            parentColumns = ["project_id"],
            childColumns = ["project"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [
        Index(value = ["project"]),
    ],
)
data class Record(
    @ColumnInfo(name = "project") val projectId: Long,
    @ColumnInfo(name = "start_time") val startTime: Instant,
    @ColumnInfo(name = "end_time") val endTime: Instant,
    @ColumnInfo(name = "date") val date: LocalDate = startTime.toLocalDateTime(TimeZone.currentSystemDefault()).date,
    @ColumnInfo(name = "record_id") @PrimaryKey(autoGenerate = true) val id: Long = 0,
)
