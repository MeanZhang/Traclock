package com.mean.traclock.utils

import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.abs

object TimeUtils {
    /**
     * 获取以整数表示的日期
     * @param timestamp 时间戳，以毫秒为单位
     * @return 以整数表示的日期，例如 `20000101`
     */
    fun getIntDate(timestamp: Long = System.currentTimeMillis()): Int {
        val time = ZonedDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault())
        return time.year * 10000 + time.monthValue * 100 + time.dayOfMonth
    }

    /**
     * 获取日期时间字符串
     * @param timestamp 时间戳（毫秒）
     * @return 日期时间字符串，格式为系统时区的 `yyyy-MM-dd HH:mm:ss`
     */
    fun getDateTimeString(timestamp: Long): String {
        val time = ZonedDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault())
        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(time)
    }

    /**
     * 获取精确到分的时间字符串
     * @param timestamp 时间戳（毫秒）
     * @return 精确到分的时间字符串，格式为系统时区的 `HH:mm`
     */
    fun getTimeString(timestamp: Long): String {
        val time = ZonedDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault())
        return DateTimeFormatter.ofPattern("HH:mm").format(time)
    }

    /**
     * 获取时长字符串
     * @param startTime 开始时间的时间戳（秒）
     * @param endTime 结束时间的时间戳（秒）
     * @return 精确到秒的时间字符串，格式为 `HH:mm:ss`
     */
    fun getMillisecondDurationString(startTime: Long, endTime: Long): String {
        val last = abs(startTime - endTime)
        val s = last % 60
        val min = (last / 60) % 60
        val h = last / 3600
        return String.format("%02d:%02d:%02d", h, min, s)
    }

    /**
     * 获取时长字符串
     * @param startTime 开始时间的时间戳
     * @param endTime 结束时间的时间戳
     * @param useMillisecond 时间戳单位是否为毫秒
     * @return 精确到秒的时间字符串，格式为 `HH:mm:ss`
     */
    fun getDurationString(startTime: Long, endTime: Long, useMillisecond: Boolean = true): String {
        val last = if (useMillisecond) abs(startTime - endTime) / 1000 else abs(startTime - endTime)
        val s = last % 60
        val min = (last / 60) % 60
        val h = last / 3600
        return String.format("%02d:%02d:%02d", h, min, s)
    }

    /**
     * 获取时长字符串
     * @param duration 时长秒数
     * @return 精确到秒的时间字符串，格式为`HH:mm:ss`
     */
    fun getDurationString(duration: Long): String {
        val s = duration % 60
        val min = (duration / 60) % 60
        val h = duration / 3600
        return String.format("%02d:%02d:%02d", h, min, s)
    }

    /**
     * 获取日期字符串
     * @param timestamp 时间戳（毫秒）
     * @return 日期字符串，格式为系统时区的日期
     */
    fun getDataString(timestamp: Long): String {
        val time = ZonedDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault())
        val today = ZonedDateTime.now(ZoneId.systemDefault())
        if (time.year == today.year && time.dayOfYear == today.dayOfYear) {
            return "今天"
        }
        val pattern = when (Locale.getDefault().language) {
            "zh" -> "MMMd日 EEE"
            else -> "EEE, MMM d"
        }
        return time.format(DateTimeFormatter.ofPattern(pattern))
    }

    /**
     * 获取日期字符串
     * @param date 以整数表示的日期，例如20211231
     * @return 日期字符串，格式为系统时区的日期
     */
    fun getDataString(date: Int): String {
        val year = date / 10000
        val month = (date / 100) % 100
        val day = date % 100
        val time = ZonedDateTime.of(year, month, day, 1, 0, 0, 0, ZoneId.systemDefault())
        val today = ZonedDateTime.now(ZoneId.systemDefault())
        if (time.year == today.year && time.dayOfYear == today.dayOfYear) {
            return "今天"
        }
        val pattern = when (Locale.getDefault().language) {
            "zh" -> "MMMd日 EEE"
            else -> "EEE, MMM d"
        }
        return time.format(DateTimeFormatter.ofPattern(pattern))
    }

    /**
     * 获取精确到秒的时间
     * @param timestamp 时间戳（毫秒）
     * @return 精确到秒的时间字符串，格式为 `HH:mm:ss`
     */
    fun getTimeWithSeconds(timestamp: Long): String {
        val time = ZonedDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault())
        return DateTimeFormatter.ofPattern("HH:mm:ss").format(time)
    }

    fun now(): Long = System.currentTimeMillis()

    /**
     * 获取当前日期时间
     * @return 日期时间字符串，格式为 `yyyyMMddHHmmss`
     */
    fun getDateTime(): String {
        val time = ZonedDateTime.now(ZoneId.systemDefault())
        return DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(time)
    }
}
