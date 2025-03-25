package com.mean.traclock.database

import androidx.room.Room
import androidx.room.RoomDatabase
import java.io.File

internal fun getDatabaseBuilder(): RoomDatabase.Builder<TraclockDatabase> {
    val dbFile = File(System.getProperty("user.home"), ".traclock/$DB_FILE_NAME")
    return Room.databaseBuilder<TraclockDatabase>(
        name = dbFile.absolutePath,
    )
}
