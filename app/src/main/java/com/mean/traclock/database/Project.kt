package com.mean.traclock.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Project(@PrimaryKey val name: String, val color: Int = 0)
