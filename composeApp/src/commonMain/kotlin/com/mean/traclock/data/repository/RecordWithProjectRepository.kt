package com.mean.traclock.data.repository

import com.mean.traclock.data.database.RecordWithProjectDao
import com.mean.traclock.model.RecordWithProject
import com.mean.traclock.utils.TimeUtils.toInt
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

class RecordWithProjectRepository(private val recordsWithProjectDao: RecordWithProjectDao) {
    /**
     * 获取指定 ID 的详细记录
     * @param id 记录 ID
     * @return 指定 ID 的详细记录
     */
    suspend fun get(id: Long): RecordWithProject = recordsWithProjectDao.get(id)

    /**
     * 获取所有详细记录
     * @return 所有详细记录
     */
    suspend fun getAll(): List<RecordWithProject> = recordsWithProjectDao.getAll()

    /**
     * 获取指定日期的详细记录
     * @param date 日期，如`20210101`
     * @return 指定日期的详细记录
     */
    fun getRecordsWithProject(date: LocalDate): Flow<List<RecordWithProject>> =
        recordsWithProjectDao.watchDayRecordsWithProject(date.toInt())

    /**
     * 获取按日期分组的详细记录
     * @return 按日期分组的详细记录
     */
    fun getDaysRecordsWithProject(): Flow<Map<Int, List<RecordWithProject>>> = recordsWithProjectDao.watchDaysRecordsWithProject()
}
