package com.mean.timepicker

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern
import kotlinx.datetime.toLocalDateTime

@OptIn(FormatStringsInDatetimeFormats::class)
internal object TimeUtils {
    /**
     * 获取日期字符串
     * @param timestamp 时间戳（毫秒）
     * @return 日期字符串，格式为`yyyy-MM-dd`
     */
    fun getDataStringWithYear(timestamp: Long): String {
        val date = Instant.fromEpochMilliseconds(timestamp).toLocalDateTime(TimeZone.currentSystemDefault()).date
        return date.format(LocalDate.Format { byUnicodePattern("yyyy-MM-dd") })
    }

    /**
     * 获取带秒的时间字符串
     * @param timeMillis 时间戳（毫秒）
     * @return 精确到秒的时间字符串，格式为 `HH:mm:ss`
     */
    fun getTimeStringWithSecond(timeMillis: Long): String {
        val time = Instant.fromEpochMilliseconds(timeMillis).toLocalDateTime(TimeZone.currentSystemDefault())
        val dateTimeFormat = LocalDateTime.Format { byUnicodePattern("HH:mm:ss") }
        return dateTimeFormat.format(time)
    }

    /**
     * 获取精确到秒的时间
     * @param time [LocalTime]
     * @return 精确到秒的时间字符串，格式为 `HH:mm:ss`
     */
    fun getTimeStringWithSecond(time: LocalTime) = time.format(LocalTime.Format { byUnicodePattern("HH:mm:ss") })
}
