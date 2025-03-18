package com.mean.traclock.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mean.traclock.model.Project
import com.mean.traclock.model.Record

@Database(
    entities = [Project::class, Record::class],
    version = 1,
    exportSchema = false,
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase(), DB {
    abstract fun projectDao(): ProjectDao

    abstract fun recordDao(): RecordDao

    abstract fun recordWithProjectDao(): RecordWithProjectDao

    override fun clearAllTables() {
        super.clearAllTables()
    }
}

// FIXME
interface DB {
    fun clearAllTables() {}
}

internal const val DB_FILE_NAME = "database.db"
