package com.mean.traclock.data.repository

import com.mean.traclock.data.database.RecordDao
import com.mean.traclock.model.ProjectDuration
import com.mean.traclock.model.Record
import com.mean.traclock.utils.TimeUtils.toInt
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDate
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class RecordsRepository(private val recordDao: RecordDao) {
    // *************
    // **** 增加 ****
    // *************

    /**
     * 增加记录
     * @param record 要增加的记录
     * @return 插入记录的 id
     */
    suspend fun insert(record: Record) = withContext(Dispatchers.IO) { recordDao.insert(record) }

    // *************
    // **** 删除 ****
    // *************

    /**
     * 删除记录
     * @param record 要删除的记录
     * @return 成功删除的行数
     */
    suspend fun delete(record: Record) = withContext(Dispatchers.IO) { recordDao.delete(record) }

    // *************
    // **** 修改 ****
    // *************

    /**
     * 更新记录
     * @param record 要更新的记录
     * @return 成功更新的行数
     */
    suspend fun update(record: Record) = withContext(Dispatchers.IO) { recordDao.update(record) }

    // *************
    // **** 查询 ****
    // *************

    // **** 记录[Record] ****

    /**
     * 获取指定 ID 的记录
     * @param id 记录 ID
     * @return 指定 ID 的记录
     */
    suspend fun get(id: Long): Record = withContext(Dispatchers.IO) { recordDao.get(id) }

    /**
     * 获取所有记录
     * @return 所有记录
     */
    suspend fun getAll() = withContext(Dispatchers.IO) { recordDao.getAll() }

    /**
     * 获取所有记录
     * @return 所有记录
     */
    fun watchAll(): Flow<List<Record>> = recordDao.watchAll()

    /**
     * 获取记录总数
     * @return 所有记录数
     */
    fun watchRecordsCount(): Flow<Int> = recordDao.watchRecordsCount()

    /**
     * 获取指定日期范围内的记录总数
     * @param startDate 开始日期，如`20210101`
     * @param endDate 结束日期，如`20210131`
     * @return 指定日期范围内的记录总数
     */
    fun watchRecordsCount(
        startDate: LocalDate,
        endDate: LocalDate,
    ): Flow<Int> = recordDao.watchRecordsCount(startDate.toInt(), endDate.toInt())

    /**
     * 获取指定日期的记录
     * @param date 日期，如`20210101`
     * @return 指定日期的记录
     */
    fun getRecords(date: LocalDate): Flow<List<Record>> = recordDao.watchDayRecords(date.toInt())

    /**
     * 获取指定日期范围内的记录
     * @param startDate 开始日期，如`20210101`
     * @param endDate 结束日期，如`20210131`
     * @return 指定日期范围内的记录
     */
    fun getRecords(
        startDate: LocalDate,
        endDate: LocalDate,
    ): Flow<List<Record>> = recordDao.watchPeriodRecords(startDate.toInt(), endDate.toInt())

    /**
     * 获取按日期分组的记录
     * @return 按日期分组的记录
     */
    fun getDaysRecords(): Flow<Map<Int, List<Record>>> = recordDao.watchDaysRecords()

    /**
     * 获取指定项目的按日期分组的记录
     * @param projectId 项目 ID
     * @return 指定项目的按日期分组的记录
     */
    fun watchProjectRecords(projectId: Long) = recordDao.watchDaysProjectRecords(projectId)

    // **** 项目时长[ProjectDurationEntry] ****

    /**
     * 获取每个项目的总时长
     * @return 所有每个的总时长
     */
    fun watchProjectsDuration(): Flow<List<ProjectDuration>> =
        recordDao.watchProjectsDuration()
            .map { it.map { projectDurationEntry -> projectDurationEntry.toProjectDuration() } }

    /**
     * 获取指定日期范围内的各项目时长
     * @param startDate 开始日期，如`20210101`
     * @param endDate 结束日期，如`20210131`
     * @return 指定日期范围内的各项目时长
     */
    fun watchProjectsDuration(
        startDate: LocalDate,
        endDate: LocalDate,
    ): Flow<List<ProjectDuration>> =
        recordDao.watchProjectsDuration(startDate.toInt(), endDate.toInt()).map {
            it.map { projectDurationEntry -> projectDurationEntry.toProjectDuration() }
        }

    /**
     * 获取每天的各项目时长
     * @return 每天的各项目时长
     */
    fun watchDaysProjectsDuration(): Flow<Map<Int, List<ProjectDuration>>> =
        recordDao.watchDaysProjectsDuration().map {
            it.mapValues { (_, value) -> value.map { projectDurationEntry -> projectDurationEntry.toProjectDuration() } }
        }

    // **** 时长 ****

    /**
     * 获取指定日期的总时长
     * @param date 日期
     * @return 指定日期的总时长
     */
    fun watchDayDuration(date: LocalDate) = recordDao.watchDayDuration(date.toInt())

    /**
     * 获取每天的总时长
     * @return 每天的总时长
     */
    fun watchDaysDuration() = recordDao.watchDaysDuration()

    /**
     * 获取指定项目的每天的总时长
     * @param projectId 项目 ID
     * @return 指定项目的每天的总时长
     */
    fun watchDaysDuration(projectId: Long) = recordDao.watchDaysDuration(projectId)

    /**
     * 获取指定日期范围内的每天的总时长
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 指定日期范围内的每天的总时长
     */
    fun watchDaysDuration(
        startDate: LocalDate,
        endDate: LocalDate,
    ) = recordDao.watchDaysDuration(startDate.toInt(), endDate.toInt())

    /**
     * 获取某年每月的总时长
     * @param year 年份
     * @return 某年每月的总时长
     */
    fun watchMonthsDuration(year: Int): Flow<Map<Int, Duration>> =
        recordDao.watchMonthsDuration(year).map {
            it.mapValues { (_, value) -> value.toDuration(DurationUnit.MILLISECONDS) }
        }

    /**
     * 获取每年的总时长
     * @return 每年的总时长
     */
    fun watchYearsDuration(): Flow<Map<Int, Duration>> =
        recordDao.watchYearsDuration().map {
            it.mapValues { (_, value) -> value.toDuration(DurationUnit.MILLISECONDS) }
        }
}
