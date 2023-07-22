package com.mean.traclock.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Fts4
import androidx.room.PrimaryKey
import com.mean.traclock.utils.TimeUtils

/**
 * @param project 项目
 * @param startTime 开始时间（毫秒）
 * @param endTime 结束时间（毫秒）
 * @param date 日期
 *
 */
@Fts4
@Entity
data class Record(
    var project: String,
    var startTime: Long,
    var endTime: Long,
    var date: Int = TimeUtils.getIntDate(startTime),
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "rowid")
    var id: Int = 0
}
