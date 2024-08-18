package com.mean.traclock.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mean.traclock.utils.TimeUtils

/**
 * @param project 项目 ID
 * @param startTime 开始时间（毫秒）
 * @param endTime 结束时间（毫秒）
 * @param date 开始日期（格式：20210101）
 * @param id 记录的 ID
 */
@Entity
data class Record(
    var project: Long,
    var startTime: Long,
    var endTime: Long,
    var date: Int = TimeUtils.getIntDate(startTime),
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
)
