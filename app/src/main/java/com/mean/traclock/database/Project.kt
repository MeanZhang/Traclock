package com.mean.traclock.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Fts4
import androidx.room.PrimaryKey

@Fts4
@Entity
data class Project(var name: String, var color: Int = 0) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "rowid")
    var id: Int = 0
}
