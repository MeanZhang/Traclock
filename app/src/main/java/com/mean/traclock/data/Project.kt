package com.mean.traclock.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Project(
    var name: String,
    var color: Int = 0,
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
)
