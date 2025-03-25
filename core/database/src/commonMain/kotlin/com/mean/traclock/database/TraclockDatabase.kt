package com.mean.traclock.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.mean.traclock.database.dao.ProjectDao
import com.mean.traclock.database.dao.RecordDao
import com.mean.traclock.database.dao.RecordWithProjectDao
import com.mean.traclock.database.model.ProjectEntry
import com.mean.traclock.database.model.RecordEntry
import kotlinx.coroutines.Dispatchers

internal const val DB_FILE_NAME = "database.db"

@Database(
    entities = [ProjectEntry::class, RecordEntry::class],
    version = 1,
    exportSchema = true,
)
@TypeConverters(Converters::class)
@ConstructedBy(TraclockDatabaseConstructor::class)
abstract class TraclockDatabase : RoomDatabase() {
    abstract fun projectDao(): ProjectDao

    abstract fun recordDao(): RecordDao

    abstract fun recordWithProjectDao(): RecordWithProjectDao
}

@Suppress("NO_ACTUAL_FOR_EXPECT")
internal expect object TraclockDatabaseConstructor : RoomDatabaseConstructor<TraclockDatabase> {
    override fun initialize(): TraclockDatabase
}

internal fun getDatabase(builder: RoomDatabase.Builder<TraclockDatabase>) =
    builder
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
