package com.mean.traclock.database

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.room3.ColumnTypeConverter
import com.mean.traclock.utils.toInt
import com.mean.traclock.utils.toLocalDate
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

internal class Converters {
    @ColumnTypeConverter
    fun colorToInt(color: Color): Int {
        return color.toArgb()
    }

    @ColumnTypeConverter
    fun intToColor(value: Int): Color {
        return Color(value)
    }

    @ColumnTypeConverter
    fun instantToLong(instant: Instant): Long {
        return instant.toEpochMilliseconds()
    }

    @ColumnTypeConverter
    fun longToInstant(value: Long): Instant {
        return Instant.fromEpochMilliseconds(value)
    }

    @ColumnTypeConverter
    fun localDateToInt(localDate: LocalDate): Int {
        return localDate.toInt()
    }

    @ColumnTypeConverter
    fun intToLocalDate(value: Int): LocalDate {
        return value.toLocalDate()
    }

    @ColumnTypeConverter
    fun longToDuration(value: Long): Duration {
        return value.toDuration(DurationUnit.MILLISECONDS)
    }

    @ColumnTypeConverter
    fun durationToLong(duration: Duration): Long {
        return duration.inWholeMilliseconds
    }
}
