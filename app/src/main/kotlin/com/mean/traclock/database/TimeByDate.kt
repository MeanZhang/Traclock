package com.mean.traclock.database

import androidx.room.ColumnInfo

data class TimeByDate(
    @ColumnInfo(name = "date") val date: Int,
    @ColumnInfo(name = "time") val time: Long
)
