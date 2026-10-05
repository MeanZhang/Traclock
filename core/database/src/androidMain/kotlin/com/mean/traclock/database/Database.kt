package com.mean.traclock.database

import android.content.Context
import androidx.room3.Room
import androidx.room3.RoomDatabase

internal fun getDatabaseBuilder(context: Context): RoomDatabase.Builder<TraclockDatabase> {
    val appContext = context.applicationContext
    val dbFile = appContext.getDatabasePath(DB_FILE_NAME)
    return Room.databaseBuilder<TraclockDatabase>(
        context = appContext,
        name = dbFile.absolutePath,
    )
}
