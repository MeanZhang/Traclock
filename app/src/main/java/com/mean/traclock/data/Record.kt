package com.mean.traclock.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Fts4
import androidx.room.PrimaryKey
import com.mean.traclock.utils.TimeUtils

/**
 * @param project 项目 ID
 * @param startTime 开始时间（毫秒）
 * @param endTime 结束时间（毫秒）
 * @param date 开始日期（格式：20210101）
 */
@Fts4
@Entity
data class Record(
    var project: Long,
    var startTime: Long,
    var endTime: Long,
    var date: Int = TimeUtils.getIntDate(startTime),
) {
    /** 记录的 ID */
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "rowid")
    var id: Long = 0
}
