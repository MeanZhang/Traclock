package com.mean.traclock.data.repository

import com.mean.traclock.data.Record
import com.mean.traclock.data.database.RecordDao
import com.mean.traclock.utils.TimeUtils.toInt
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDate

class RecordsRepository(private val recordDao: RecordDao) {
    suspend fun insert(record: Record) = withContext(Dispatchers.IO) { recordDao.insert(record) }

    fun getRecordsOfDays() = recordDao.getRecordsOfDays()

    fun getRecordsOfDays(id: Long) = recordDao.getRecordsOfDays(id)

    fun getProjectsTimeOfDays() = recordDao.getProjectsTimeOfDays()

    fun getTimeOfDays() = recordDao.getTimeOfDays()

    fun getTimeOfDays(id: Long) = recordDao.getTimeOfDays(id)

    fun getProjectsTime(): Flow<List<Record>> = recordDao.getProjectsTime()

    suspend fun delete(record: Record) = withContext(Dispatchers.IO) { recordDao.delete(record) }

    suspend fun get(id: Long): Record = withContext(Dispatchers.IO) { recordDao.get(id) }

    suspend fun update(record: Record) = withContext(Dispatchers.IO) { recordDao.update(record) }

    suspend fun getRecordsList() = withContext(Dispatchers.IO) { recordDao.getRecordsList() }

    fun getProjectsTimeOfPeriod(
        startDate: LocalDate,
        endDate: LocalDate,
    ) = recordDao.getProjectsTimeOfPeriod(startDate.toInt(), endDate.toInt())

    fun getAllRecordsNumber(): Flow<Int> = recordDao.getAllRecordsNumber()

    fun getRecordsNumber(
        startDate: LocalDate,
        endDate: LocalDate,
    ): Flow<Int> = recordDao.getRecordsNumber(startDate.toInt(), endDate.toInt())

    fun getRecords(date: LocalDate): Flow<List<Record>> = recordDao.getRecords(date.toInt())

    fun getRecords(
        startDate: LocalDate,
        endDate: LocalDate,
    ): Flow<List<Record>> = recordDao.getRecords(startDate.toInt(), endDate.toInt())

    fun getAllRecords(): Flow<List<Record>> = recordDao.getAllRecords()

    fun getDurationsOfYears(): Flow<Map<Int, Long>> = recordDao.getDurationsOfYears()
}
