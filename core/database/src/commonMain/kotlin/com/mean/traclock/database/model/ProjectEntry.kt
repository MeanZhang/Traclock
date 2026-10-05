package com.mean.traclock.database.model

import androidx.compose.ui.graphics.Color
import androidx.room3.ColumnInfo
import androidx.room3.Entity
import androidx.room3.PrimaryKey
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
