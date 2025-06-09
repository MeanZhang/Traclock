package com.mean.traclock.utils

import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
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
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import java.util.Locale
import kotlin.time.Duration

/**
 * 将[LocalDate]转换为整数，例如 `20000101`
 */
fun LocalDate.toInt(): Int = year * 10000 + monthNumber * 100 + dayOfMonth

/**
 * 将整数转换为[LocalDate]
 */
fun Int.toLocalDate(): LocalDate {
    val year = this / 10000
    val month = (this / 100) % 100
    val day = this % 100
    return LocalDate(year, month, day)
}

@OptIn(FormatStringsInDatetimeFormats::class)
object TimeUtils {
    @JvmStatic
    val CHINESE_DAY_OF_WEEK_NAMES =
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

    /**
     * 日期格式，例如 `12月31日 星期五`
     */
    @JvmStatic
    private val DATE_FORMAT =
        LocalDate.Format {
//        byUnicodePattern(
//                when (Locale.current.language) {
//                    "zh" -> "MMMd日 EEE"
//                    else -> "EEE, MMM d"
//                }
//        )
            when (Locale.getDefault().language) {
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
     * 不带星期几的日期格式，例如 `12月31日`
     */
    @JvmStatic
    private val DATE_FORMAT_WITHOUT_DAY_OF_WEEK =
        LocalDate.Format {
            when (Locale.getDefault().language) {
                "zh" -> {
                    monthNumber(padding = Padding.NONE)
                    char('月')
                    dayOfMonth(padding = Padding.NONE)
                    chars("日")
                }

                else -> {
                    monthName(MonthNames.ENGLISH_ABBREVIATED)
                    char(' ')
                    dayOfMonth(Padding.NONE)
                }
            }
        }

    /**
     * 月份格式，例如 `2021年12月`
     */
    @JvmStatic
    private val MONTH_FORMAT =
        LocalDate.Format {
            when (Locale.getDefault().language) {
                "zh" -> {
                    year(padding = Padding.NONE)
                    char('年')
                    monthNumber(padding = Padding.NONE)
                    char('月')
                }

                else -> {
                    monthName(MonthNames.ENGLISH_ABBREVIATED)
                    char(' ')
                    year(padding = Padding.NONE)
                }
            }
        }

    /**
     * 日期格式，例如 `2021年12月`
     */
    @JvmStatic
    private val ALL_TIME_DATE_FORMAT =
        LocalDate.Format {
            when (Locale.getDefault().language) {
                "zh" -> {
                    year(padding = Padding.NONE)
                    char('年')
                    monthNumber(padding = Padding.NONE)
                    char('月')
                    dayOfMonth(padding = Padding.NONE)
                    char('日')
                }

                else -> {
                    dayOfMonth()
                    char(' ')
                    monthName(MonthNames.ENGLISH_ABBREVIATED)
                    char(' ')
                    year(padding = Padding.NONE)
                }
            }
        }

    @JvmStatic
    fun getCurrentYear(): Int = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date.year

    /**
     * 将[LocalDate]转换为整数，例如 `20000101`
     */
    @JvmStatic
    fun LocalDate.toInt(): Int = year * 10000 + monthNumber * 100 + dayOfMonth

    /**
     * 获取以整数表示的日期
     * @param timestamp 时间戳，以毫秒为单位
     * @return 以整数表示的日期，例如 `20000101`
     */
    @JvmStatic
    fun getIntDate(timestamp: Long = now().toEpochMilliseconds()): Int {
        val time = Instant.fromEpochMilliseconds(timestamp).toLocalDateTime(TimeZone.currentSystemDefault())
        return time.date.toInt()
    }

    /**
     * 获取日期时间字符串
     * @param instant 时间戳（毫秒）
     * @return 日期时间字符串，格式为系统时区的 `yyyy/M/d H:mm:ss`
     */
    @JvmStatic
    fun getDateTimeString(instant: Instant): String {
        val dateTimeFormat = LocalDateTime.Format { byUnicodePattern("yyyy/M/d H:mm:ss") }
        val time = instant.toLocalDateTime(TimeZone.currentSystemDefault())
        return dateTimeFormat.format(time)
    }

    /**
     * 获取精确到分的时间字符串
     * @param instant 时间戳（毫秒）
     * @return 精确到分的时间字符串，格式为系统时区的 `HH:mm`
     */
    @JvmStatic
    fun getTimeString(instant: Instant): String {
        val dateTimeFormat = LocalDateTime.Format { byUnicodePattern("HH:mm") }
        val time = instant.toLocalDateTime(TimeZone.currentSystemDefault())
        return dateTimeFormat.format(time)
    }

    /**
     * 获取时长字符串
     * @param startTime 开始时间的时间戳（毫秒）
     * @param endTime 结束时间的时间戳（毫秒）
     * @return 精确到秒的时间字符串，格式为 `HH:mm:ss`
     */
    @JvmStatic
    fun getDurationString(
        startTime: Instant,
        endTime: Instant,
    ): String = getDurationString(endTime - startTime)

    /**
     * 获取简短的时长字符串
     */
    @JvmStatic
    fun getShortDurationString(
        duration: Duration,
        maxDuration: Duration,
    ): String {
        val hours = duration.inWholeHours
        val minutes = duration.inWholeMinutes % 60
        val seconds = duration.inWholeSeconds % 60
        if (maxDuration.inWholeHours > 100) {
            return "${hours}小时"
        } else if (maxDuration.inWholeHours > 0) {
            return "${hours}小时${minutes}分"
        } else if (maxDuration.inWholeMinutes > 0) {
            return "${minutes}分"
        } else {
            return "${seconds}秒"
        }
    }

    /**
     * 获取时长字符串
     * @param duration 时长毫秒数
     * @return 精确到秒的时间字符串，格式为`HH:mm:ss`
     */
    @JvmStatic
    fun getDurationString(duration: Duration): String {
        return duration.toComponents { hours, minutes, seconds, _ ->
            "${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}:${
                seconds.toString().padStart(2, '0')
            }"
        }
    }

    /**
     * 获取带年份的日期字符串
     * @param timestamp 时间戳（毫秒）
     * @return 日期字符串，格式为`yyyy-MM-dd`
     */
    @JvmStatic
    fun getDateStringWithYear(timestamp: Long): String {
        val date = Instant.fromEpochMilliseconds(timestamp).toLocalDateTime(TimeZone.currentSystemDefault()).date
        return date.format(LocalDate.Format { byUnicodePattern("yyyy-MM-dd") })
    }

    /**
     * 获取日期字符串
     * @param dateInt 以整数表示的日期，例如20211231
     * @return 日期字符串，格式为系统时区的日期
     */
    @JvmStatic
    fun getDateString(dateInt: Int): String = getDateString(getDate(dateInt))

    /**
     * 获取用于显示的日期字符串，例如`今天`或`12月31日星期五`或`2021年12月31日星期五`
     * @param date [LocalDate]
     * @return 日期字符串
     */
    @JvmStatic
    fun getDisplayDate(date: LocalDate): String {
        val today = getToday()
        if (today == date) {
            return "今天"
        }
        val format =
            LocalDate.Format {
                if (today.year != date.year) {
                    year(padding = Padding.NONE)
                    char('年')
                }
                monthNumber(padding = Padding.NONE)
                char('月')
                dayOfMonth(padding = Padding.NONE)
                char('日')
                dayOfWeek(CHINESE_DAY_OF_WEEK_NAMES)
            }
        return format.format(date)
    }

    /**
     * 获取用于显示的时间段字符串，例如`2021年1月1日至2022年1月1日`、`2021年1月1日至2月1日`、`2021年1月1日至2日`、`1月1日至2月1日`、`1月1日至2日`
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 时间段字符串
     */
    @JvmStatic
    fun getDisplayPeriod(
        startDate: LocalDate,
        endDate: LocalDate,
    ): String {
        val today = getToday()
        val start =
            if (today == startDate) {
                "今天"
            } else {
                val startFormat =
                    LocalDate.Format {
                        if (today.year != startDate.year || startDate.year != endDate.year) {
                            year(padding = Padding.NONE)
                            char('年')
                        }
                        monthNumber(padding = Padding.NONE)
                        char('月')
                        dayOfMonth(padding = Padding.NONE)
                        char('日')
                    }
                startFormat.format(startDate)
            }
        if (startDate == endDate) {
            return start
        }
        val end =
            if (today == endDate) {
                "今天"
            } else {
                val endFormat =
                    LocalDate.Format {
                        val showYear = startDate.year != endDate.year
                        if (showYear) {
                            year(padding = Padding.NONE)
                            char('年')
                        }
                        if (startDate.month != endDate.month) {
                            monthNumber(padding = Padding.NONE)
                            char('月')
                        }
                        dayOfMonth(padding = Padding.NONE)
                        char('日')
                    }
                endFormat.format(endDate)
            }
        return "${start}至$end"
    }

    /**
     * 获取日期
     * @param dateInt 以整数表示的日期，例如20211231
     * @return [LocalDate]
     */
    @JvmStatic
    fun getDate(dateInt: Int): LocalDate {
        try {
            val year = dateInt / 10000
            val month = (dateInt / 100) % 100
            val day = dateInt % 100
            return LocalDate(year, month, day)
        } catch (e: Exception) {
            return LocalDate(2021, 1, 1)
        }
    }

    /**
     * 获取日期所在星期的星期一
     * @param date [LocalDate]
     */
    @JvmStatic
    fun getMonday(date: LocalDate): LocalDate {
        val dayOfWeek = date.dayOfWeek.value
        val monday = date.minus(dayOfWeek - 1, DateTimeUnit.DAY)
        return monday
    }

    /**
     * 获取日期所在星期的星期日
     * @param date [LocalDate]
     */
    @JvmStatic
    fun getFirstDayOfMonth(date: LocalDate): LocalDate = date.minus(date.dayOfMonth - 1, DateTimeUnit.DAY)

    /**
     * 获取日期所在月的最后一天
     * @param date [LocalDate]
     */
    @JvmStatic
    fun getLastDayOfMonth(date: LocalDate): LocalDate {
        val nextMonth = getFirstDayOfMonth(date).plus(1, DateTimeUnit.MONTH)
        return nextMonth.minus(1, DateTimeUnit.DAY)
    }

    /**
     * 获取日期所在周的最后一天
     * @param date [LocalDate]
     */
    @JvmStatic
    fun getLastDayOfWeek(date: LocalDate): LocalDate {
        return LocalDate(date.year, date.month, date.dayOfMonth - date.dayOfWeek.value)
    }

    /**
     * 获取日期所在年的第一天
     * @param date [LocalDate]
     */
    @JvmStatic
    fun getFirstDayOfYear(date: LocalDate): LocalDate {
        val year = date.year
        return LocalDate(year, 1, 1)
    }

    /**
     * 获取日期所在年的最后一天
     * @param date [LocalDate]
     */
    @JvmStatic
    fun getLastDayOfYear(date: LocalDate): LocalDate {
        val nextYear = getFirstDayOfYear(date).plus(1, DateTimeUnit.YEAR)
        return nextYear.minus(1, DateTimeUnit.DAY)
    }

    /**
     * 获取日期字符串
     * @param date [LocalDate]
     * @return 日期字符串，格式为系统时区的日期
     */
    @JvmStatic
    fun getDateString(date: LocalDate): String {
        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        if (date.compareTo(today) == 0) {
            return if (Locale.getDefault().language == "zh") "今天" else "Today"
        }
        return DATE_FORMAT.format(date)
    }

    /**
     * 获取不带星期几的日期字符串
     * @param date [LocalDate]
     * @return 日期字符串，格式为系统时区的日期
     */
    @JvmStatic
    fun getDateStringWithoutDayOfWeek(date: LocalDate): String {
        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        if (date.compareTo(today) == 0) {
            return if (Locale.getDefault().language == "zh") "今天" else "Today"
        }
        return DATE_FORMAT_WITHOUT_DAY_OF_WEEK.format(date)
    }

    @JvmStatic
    fun getMonthString(date: LocalDate): String {
        return MONTH_FORMAT.format(date)
    }

    @JvmStatic
    fun getAllTimeDateString(date: LocalDate): String {
        return ALL_TIME_DATE_FORMAT.format(date)
    }

    /**
     * 获取带秒的时间字符串
     * @param timeMillis 时间戳（毫秒）
     * @return 精确到秒的时间字符串，格式为 `HH:mm:ss`
     */
    @JvmStatic
    fun getTimeStringWithSecond(timeMillis: Long): String {
        val time = Instant.fromEpochMilliseconds(timeMillis).toLocalDateTime(TimeZone.currentSystemDefault())
        val dateTimeFormat = LocalDateTime.Format { byUnicodePattern("HH:mm:ss") }
        return dateTimeFormat.format(time)
    }

    /**
     * 获取当前时间戳
     * @return 当前时间戳（毫秒）
     */
    @JvmStatic
    fun now(): Instant = Clock.System.now()

    /**
     * 获取给定时间所在当前时区日期的UTC日期的时间戳
     * @param timeMillis 时间戳（毫秒）
     * @return UTC日期的时间戳（毫秒）
     */
    @JvmStatic
    fun getUtcDateMillis(timeMillis: Long): Long {
        val date = Instant.fromEpochMilliseconds(timeMillis).toLocalDateTime(TimeZone.currentSystemDefault()).date
        return date.atStartOfDayIn(TimeZone.UTC).toEpochMilliseconds()
    }

    /**
     * 将给定的UTC日期的时间戳转换为[LocalDate]
     * @param dateMillis UTC日期的时间戳（毫秒）
     * @return [LocalDate]
     */
    @JvmStatic
    fun utcMillisToLocalDate(dateMillis: Long): LocalDate {
        return Instant.fromEpochMilliseconds(dateMillis).toLocalDateTime(TimeZone.UTC).date
    }

    /**
     * 获取日期时间字符串，不带分隔符
     * @return 日期时间字符串，格式为`yyyyMMdd_HHmmss`
     */
    @JvmStatic
    fun getDateTimeStringWithoutSeperator(): String {
        val dateTimeFormat = LocalDateTime.Format { byUnicodePattern("yyyyMMdd_HHmmss") }
        val time = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        return dateTimeFormat.format(time)
    }

    /**
     * 获取当前日期
     * @return [LocalDate]
     */
    @JvmStatic
    fun getToday(): LocalDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

    fun getMillisOfDay(instant: Instant): Int {
        val time = instant.toLocalDateTime(TimeZone.currentSystemDefault()).time
        return time.toMillisecondOfDay()
    }

    fun getDayOfWeek(date: LocalDate): Int = date.dayOfWeek.value

    fun getDayOfWeek(date: Int): Int = getDate(date).dayOfWeek.value
}
