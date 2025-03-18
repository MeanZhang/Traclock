package com.mean.traclock.model

import androidx.compose.ui.graphics.Color
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Project(
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "color") var color: Color = Color.Unspecified,
    @ColumnInfo(name = "project_id") @PrimaryKey(autoGenerate = true) var id: Long = 0,
)
