package com.mean.traclock.data.database

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.room.TypeConverter
import com.mean.traclock.utils.TimeUtils
import com.mean.traclock.utils.TimeUtils.toInt
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class Converters {
    @TypeConverter
    fun colorToInt(color: Color): Int {
        return color.toArgb()
    }

    @TypeConverter
    fun intToColor(value: Int): Color {
        return Color(value)
    }

    @TypeConverter
    fun instantToLong(instant: Instant): Long {
        return instant.toEpochMilliseconds()
    }

    @TypeConverter
    fun longToInstant(value: Long): Instant {
        return Instant.fromEpochMilliseconds(value)
    }

    @TypeConverter
    fun localDateToInt(localDate: LocalDate): Int {
        return localDate.toInt()
    }

    @TypeConverter
    fun intToLocalDate(value: Int): LocalDate {
        return TimeUtils.getDate(value)
    }

    @TypeConverter
    fun longToDuration(value: Long): Duration {
        return value.toDuration(DurationUnit.MILLISECONDS)
    }

    @TypeConverter
    fun durationToLong(duration: Duration): Long {
        return duration.inWholeMilliseconds
    }
}
