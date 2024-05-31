package com.mean.traclock.data.repository

import com.mean.traclock.data.Record
import com.mean.traclock.data.database.RecordDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecordsRepository
    @Inject
    constructor(private val recordDao: RecordDao) {
        suspend fun insert(record: Record) = recordDao.insert(record)

        fun getRecordsOfDays() = recordDao.getRecordsOfDays()

        fun getRecordsOfDays(id: Long) = recordDao.getRecordsOfDays(id)

        fun getProjectsTimeOfDays() = recordDao.getProjectsTimeOfDays()

        fun getTimeOfDays() = recordDao.getTimeOfDays()

        fun getTimeOfDays(id: Long) = recordDao.getTimeOfDays(id)

        fun getProjectsTime(): Flow<List<Record>> = recordDao.getProjectsTime()

        suspend fun delete(record: Record) = recordDao.delete(record)

        fun getProjectsTimeOfDay(date: Int) = recordDao.getProjectsTimeOfDay(date)

        suspend fun get(id: Long): Record = recordDao.get(id)

        suspend fun update(record: Record) = recordDao.update(record)

        suspend fun getRecordsList() = recordDao.getRecordsList()
    }
