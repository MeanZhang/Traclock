package com.mean.traclock.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mean.traclock.data.Project
import com.mean.traclock.data.Record

@Database(
    entities = [Project::class, Record::class],
    version = 1,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun projectDao(): ProjectDao

    abstract fun recordDao(): RecordDao
}
