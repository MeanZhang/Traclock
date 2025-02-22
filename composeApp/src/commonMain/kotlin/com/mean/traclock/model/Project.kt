package com.mean.traclock.model

import androidx.compose.ui.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Project(
    var name: String,
    var color: Color = Color.Unspecified,
    @PrimaryKey(autoGenerate = true)
    var projectId: Long = 0,
)
