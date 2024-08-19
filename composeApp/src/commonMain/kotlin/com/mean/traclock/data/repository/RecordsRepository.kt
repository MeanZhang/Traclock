package com.mean.traclock.data.repository

import com.mean.traclock.data.Record
import com.mean.traclock.data.database.RecordDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class RecordsRepository(private val recordDao: RecordDao) {
    suspend fun insert(record: Record) = withContext(Dispatchers.IO) { recordDao.insert(record) }

    fun getRecordsOfDays() = recordDao.getRecordsOfDays()

    fun getRecordsOfDays(id: Long) = recordDao.getRecordsOfDays(id)

    fun getProjectsTimeOfDays() = recordDao.getProjectsTimeOfDays()

    fun getTimeOfDays() = recordDao.getTimeOfDays()

    fun getTimeOfDays(id: Long) = recordDao.getTimeOfDays(id)

    fun getProjectsTime(): Flow<List<Record>> = recordDao.getProjectsTime()

    suspend fun delete(record: Record) = withContext(Dispatchers.IO) { recordDao.delete(record) }

    fun getProjectsTimeOfDay(date: Int) = recordDao.getProjectsTimeOfDay(date)

    suspend fun get(id: Long): Record = withContext(Dispatchers.IO) { recordDao.get(id) }

    suspend fun update(record: Record) = withContext(Dispatchers.IO) { recordDao.update(record) }

    suspend fun getRecordsList() = withContext(Dispatchers.IO) { recordDao.getRecordsList() }
}
