package com.mean.traclock.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mean.traclock.data.Record
import data.Project

@Database(
    entities = [Project::class, Record::class],
    version = 1,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase(), DB {
    abstract fun projectDao(): ProjectDao

    abstract fun recordDao(): RecordDao

    override fun clearAllTables() {
        super.clearAllTables()
    }
}

// FIXME
interface DB {
    fun clearAllTables() {}
}

internal const val DB_FILE_NAME = "database.db"
