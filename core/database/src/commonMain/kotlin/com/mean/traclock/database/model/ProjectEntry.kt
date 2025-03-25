package com.mean.traclock.database.model

import androidx.compose.ui.graphics.Color
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mean.traclock.model.Project

@Entity(tableName = "projects")
data class ProjectEntry(
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "color") val color: Color,
    @ColumnInfo(name = "project_id") @PrimaryKey(autoGenerate = true) val id: Long = 0,
)

fun ProjectEntry.asExternalProject() =
    Project(
        name = name,
        color = color,
        id = id,
    )
