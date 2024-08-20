package com.mean.traclock.utils

import androidx.compose.ui.text.intl.Locale
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.format
import kotlinx.datetime.format.DayOfWeekNames
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.Padding
import kotlinx.datetime.format.byUnicodePattern
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime
import kotlin.math.abs
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@OptIn(FormatStringsInDatetimeFormats::class)
object TimeUtils {
    private val CHINESE_DAY_OF_WEEK_NAMES =
        DayOfWeekNames(
            listOf(
                "星期一",
                "星期二",
                "星期三",
                "星期四",
                "星期五",
                "星期六",
                "星期日",
            ),
        )
    private val DATE_FORMAT =
        LocalDate.Format {
//        byUnicodePattern(
//                when (Locale.current.language) {
//                    "zh" -> "MMMd日 EEE"
//                    else -> "EEE, MMM d"
//                }
//        )
            when (Locale.current.language) {
                "zh" -> {
                    monthNumber(padding = Padding.NONE)
                    char('月')
                    dayOfMonth(padding = Padding.NONE)
                    chars("日 ")
                    dayOfWeek(CHINESE_DAY_OF_WEEK_NAMES)
                }

                else -> {
                    dayOfWeek(DayOfWeekNames.ENGLISH_ABBREVIATED)
                    chars(", ")
                    monthName(MonthNames.ENGLISH_ABBREVIATED)
                    char(' ')
                    dayOfMonth(Padding.NONE)
                }
            }
        }

    /**
     * 获取以整数表示的日期
     * @param timestamp 时间戳，以毫秒为单位
     * @return 以整数表示的日期，例如 `20000101`
     */
    fun getIntDate(timestamp: Long = now()): Int {
        val time = Instant.fromEpochMilliseconds(timestamp).toLocalDateTime(TimeZone.currentSystemDefault())
        return time.year * 10000 + time.monthNumber * 100 + time.dayOfMonth
    }

    /**
     * 获取日期时间字符串
     * @param timeMillis 时间戳（毫秒）
     * @return 日期时间字符串，格式为系统时区的 `yyyy/M/d H:mm:ss`
     */
    fun getDateTimeString(timeMillis: Long): String {
        val dateTimeFormat = LocalDateTime.Format { byUnicodePattern("yyyy/M/d H:mm:ss") }
        val time = Instant.fromEpochMilliseconds(timeMillis).toLocalDateTime(TimeZone.currentSystemDefault())
        return dateTimeFormat.format(time)
    }

    /**
     * 获取精确到分的时间字符串
     * @param timestamp 时间戳（毫秒）
     * @return 精确到分的时间字符串，格式为系统时区的 `HH:mm`
     */
    fun getTimeString(timestamp: Long): String {
        val dateTimeFormat = LocalDateTime.Format { byUnicodePattern("HH:mm") }
        val time = Instant.fromEpochMilliseconds(timestamp).toLocalDateTime(TimeZone.currentSystemDefault())
        return dateTimeFormat.format(time)
    }

    /**
     * 获取时长字符串
     * @param startTime 开始时间的时间戳
     * @param endTime 结束时间的时间戳
     * @param useMillisecond 时间戳单位是否为毫秒
     * @return 精确到秒的时间字符串，格式为 `HH:mm:ss`
     */
    fun getDurationString(
        startTime: Long,
        endTime: Long,
        useMillisecond: Boolean = true,
    ): String {
        val last = if (useMillisecond) abs(startTime - endTime) / 1000 else abs(startTime - endTime)
        return getDurationString(last)
    }

    /**
     * 获取时长字符串
     * @param duration 时长秒数
     * @return 精确到秒的时间字符串，格式为`HH:mm:ss`
     */
    fun getDurationString(duration: Long): String {
        val d = duration.toDuration(DurationUnit.SECONDS)
        return d.toComponents { hours, minutes, seconds, _ ->
            "${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}:${
                seconds.toString().padStart(2, '0')
            }"
        }
    }

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
     * 获取带年份的日期字符串
     * @param timestamp 时间戳（毫秒）
     * @return 带年份的日期字符串，格式为系统时区的日期
     */
    fun getDataString(timestamp: Long): String {
        val date = getDate(timestamp)
        return getDataString(date)
    }

    /**
     * 获取日期字符串
     * @param dateInt 以整数表示的日期，例如20211231
     * @return 日期字符串，格式为系统时区的日期
     */
    fun getDataString(dateInt: Int): String {
        val year = dateInt / 10000
        val month = (dateInt / 100) % 100
        val day = dateInt % 100
        val date = LocalDate(year, month, day)
        return getDataString(date)
    }

    /**
     * 获取日期字符串
     * @param date [LocalDate]
     * @return 日期字符串，格式为系统时区的日期
     */
    private fun getDataString(date: LocalDate): String {
        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        if (date.compareTo(today) == 0) {
            return if (Locale.current.language == "zh") "今天" else "Today"
        }
        return DATE_FORMAT.format(date)
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
     * 获取当前时间戳
     * @return 当前时间戳（毫秒）
     */
    fun now(): Long = Clock.System.now().toEpochMilliseconds()

    private fun getDate(timestamp: Long) = Instant.fromEpochMilliseconds(timestamp).toLocalDateTime(TimeZone.currentSystemDefault()).date

    /**
     * 获取给定时间所在当前时区日期的UTC日期的时间戳
     * @param timeMillis 时间戳（毫秒）
     * @return UTC日期的时间戳（毫秒）
     */
    fun getUtcDateMillis(timeMillis: Long): Long {
        val date = Instant.fromEpochMilliseconds(timeMillis).toLocalDateTime(TimeZone.currentSystemDefault()).date
        return date.atStartOfDayIn(TimeZone.UTC).toEpochMilliseconds()
    }

    /**
     * 将给定的UTC日期的时间戳转换为[LocalDate]
     * @param dateMillis UTC日期的时间戳（毫秒）
     * @return [LocalDate]
     */
    fun utcMillisToLocalDate(dateMillis: Long): LocalDate {
        return Instant.fromEpochMilliseconds(dateMillis).toLocalDateTime(TimeZone.UTC).date
    }

    fun getDateTimeStringWithoutSeperator(): String {
        val dateTimeFormat = LocalDateTime.Format { byUnicodePattern("yyyyMMdd_HHmmss") }
        val time = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        return dateTimeFormat.format(time)
    }
}
