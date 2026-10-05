package com.mean.traclock.database.model

import androidx.room3.ColumnInfo
import androidx.room3.Entity
import androidx.room3.ForeignKey
import androidx.room3.Index
import androidx.room3.PrimaryKey
import com.mean.traclock.model.Record
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
    tableName = "records",
    foreignKeys = [
        ForeignKey(
            entity = ProjectEntry::class,
            parentColumns = ["project_id"],
            childColumns = ["project"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [
        Index(value = ["project"]),
    ],
)
data class RecordEntry(
    @ColumnInfo(name = "project") val projectId: Long,
    @ColumnInfo(name = "start_time") val startTime: Instant,
    @ColumnInfo(name = "end_time") val endTime: Instant,
    @ColumnInfo(name = "date") val date: LocalDate = startTime.toLocalDateTime(TimeZone.currentSystemDefault()).date,
    @ColumnInfo(name = "record_id") @PrimaryKey(autoGenerate = true) val id: Long = 0,
)

fun RecordEntry.asExternalRecord() =
    Record(
        projectId = projectId,
        startTime = startTime,
        endTime = endTime,
        date = date,
        id = id,
    )
