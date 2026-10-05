package com.mean.traclock.database

import androidx.room3.Room
import androidx.room3.RoomDatabase
import java.io.File

internal fun getDatabaseBuilder(): RoomDatabase.Builder<TraclockDatabase> {
    val dbFile = File(System.getProperty("user.home"), ".traclock/$DB_FILE_NAME")
    return Room.databaseBuilder<TraclockDatabase>(
        name = dbFile.absolutePath,
    )
}
