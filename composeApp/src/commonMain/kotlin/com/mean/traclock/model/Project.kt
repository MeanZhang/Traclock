package com.mean.traclock.model

import androidx.compose.ui.graphics.Color
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Project(
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "color") val color: Color = Color.Unspecified,
    @ColumnInfo(name = "project_id") @PrimaryKey(autoGenerate = true) val id: Long = 0,
)
