package com.mean.traclock.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Fts4
import androidx.room.PrimaryKey
import com.mean.traclock.util.getIntDate

@Fts4
@Entity
data class Record(
    var project: String,
    var startTime: Long,
    var endTime: Long,
    var date: Int = getIntDate(startTime)
){
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "rowid")
    var id: Int = 0
}
