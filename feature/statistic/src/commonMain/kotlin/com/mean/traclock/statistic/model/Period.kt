package com.mean.traclock.statistic.model

import com.mean.traclock.utils.TimeUtils
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import kotlinx.datetime.plus

data class Period(
    val type: PeriodType,
    val startDate: LocalDate = TimeUtils.getToday(),
    val endDate: LocalDate = TimeUtils.getToday(),
) {
    fun changeType(newType: PeriodType): Period {
        var newPeriod = this.copy(type = newType)
        val today = TimeUtils.getToday()
        when (newType) {
            PeriodType.DAY -> {
                newPeriod = newPeriod.copy(startDate = today, endDate = today)
            }

            PeriodType.WEEK -> {
                val start = TimeUtils.getMonday(today)
                val end = start.plus(6, DateTimeUnit.DAY)
                newPeriod = newPeriod.copy(startDate = start, endDate = end)
            }

            PeriodType.MONTH -> {
                val start = TimeUtils.getFirstDayOfMonth(today)
                val end = TimeUtils.getLastDayOfMonth(start)
                newPeriod = newPeriod.copy(startDate = start, endDate = end)
            }

            PeriodType.YEAR -> {
                val start = TimeUtils.getFirstDayOfYear(today)
                val end = TimeUtils.getLastDayOfYear(start)
                newPeriod = newPeriod.copy(startDate = start, endDate = end)
            }

            PeriodType.ALL_TIME -> {}
        }
        return newPeriod
    }

    val prev: Period
        get() =
            when (type) {
                PeriodType.DAY -> {
                    val unit = DateTimeUnit.DAY
                    val date = startDate.minus(1, unit)
                    copy(startDate = date, endDate = date)
                }

                PeriodType.WEEK -> {
                    val unit = DateTimeUnit.WEEK
                    val start = startDate.minus(1, unit)
                    val end = start.plus(6, DateTimeUnit.DAY)
                    copy(startDate = start, endDate = end)
                }

                PeriodType.MONTH -> {
                    val start = startDate.minus(1, DateTimeUnit.MONTH)
                    copy(startDate = start, endDate = TimeUtils.getLastDayOfMonth(start))
                }

                PeriodType.YEAR -> {
                    val start = startDate.minus(1, DateTimeUnit.YEAR)
                    copy(startDate = start, endDate = TimeUtils.getLastDayOfYear(start))
                }

                PeriodType.ALL_TIME -> this
            }

    val next: Period
        get() {
            return when (type) {
                PeriodType.DAY -> {
                    val unit = DateTimeUnit.DAY
                    copy(startDate = startDate.plus(1, unit), endDate = endDate.plus(1, unit))
                }

                PeriodType.WEEK -> {
                    val unit = DateTimeUnit.WEEK
                    val start = startDate.plus(1, unit)
                    val end = start.plus(6, DateTimeUnit.DAY)
                    copy(startDate = start, endDate = end)
                }

                PeriodType.MONTH -> {
                    val start = startDate.plus(1, DateTimeUnit.MONTH)
                    val end = TimeUtils.getLastDayOfMonth(start)
                    copy(startDate = start, endDate = end)
                }

                PeriodType.YEAR -> {
                    val start = startDate.plus(1, DateTimeUnit.YEAR)
                    val end = TimeUtils.getLastDayOfYear(start)
                    copy(startDate = start, endDate = end)
                }

                PeriodType.ALL_TIME -> this
            }
        }

    val hasNext: Boolean
        get() =
            when (type) {
                PeriodType.DAY -> TimeUtils.getToday() > endDate
                PeriodType.WEEK -> TimeUtils.getToday() > endDate
                PeriodType.MONTH -> TimeUtils.getToday() > endDate
                PeriodType.YEAR -> TimeUtils.getToday() > endDate
                PeriodType.ALL_TIME -> false
            }

    /**
     * 计算时间戳所在范围
     */
    fun range(time: Long) = when (type) {
        PeriodType.DAY -> {
            TimeUtils.utcMillisToLocalDate(time).let { copy(startDate = it, endDate = it) }
        }

        PeriodType.WEEK -> {
            val currentDate = TimeUtils.utcMillisToLocalDate(time)
            val unit = DateTimeUnit.DAY
            val start = currentDate.minus(currentDate.dayOfWeek.value - 1, unit)
            val end = currentDate.plus(7 - currentDate.dayOfWeek.value, unit)
            copy(startDate = start, endDate = end)
        }

        PeriodType.MONTH -> {
            val currentDate = TimeUtils.utcMillisToLocalDate(time)
            val start = currentDate.minus(currentDate.dayOfMonth - 1, DateTimeUnit.DAY)
            val end = TimeUtils.getLastDayOfMonth(currentDate)
            copy(startDate = start, endDate = end)
        }

        else -> this
    }


    fun getString(allTimeHasRange: Boolean = false): String {
        return when (type) {
            PeriodType.DAY -> TimeUtils.getDisplayDate(startDate)
            PeriodType.WEEK ->TimeUtils.getDisplayPeriod(startDate, endDate)
            PeriodType.MONTH -> TimeUtils.getMonthString(startDate)
            PeriodType.YEAR -> startDate.year.toString()
            PeriodType.ALL_TIME ->
                if (allTimeHasRange)
                    TimeUtils.getAllTimeDateString(startDate) +
                            " - " +
                            TimeUtils.getAllTimeDateString(endDate)
                else "全时段"
        }
    }

    fun getDays(): List<LocalDate> {
        val days = mutableListOf<LocalDate>()
        var date = startDate
        while (date <= endDate) {
            days.add(date)
            date = date.plus(1, DateTimeUnit.DAY)
        }
        return days
    }
}
