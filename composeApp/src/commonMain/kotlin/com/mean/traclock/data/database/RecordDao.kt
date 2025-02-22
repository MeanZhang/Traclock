package com.mean.traclock.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.MapColumn
import androidx.room.Query
import androidx.room.Update
import com.mean.traclock.model.ProjectDurationEntry
import com.mean.traclock.model.Record
import kotlinx.coroutines.flow.Flow

@Dao
interface RecordDao {
    // *************
    // **** 增加 ****
    // *************

    /**
     * 增加记录
     * @param record 要增加的记录
     * @return 插入记录的 id
     */
    @Insert
    suspend fun insert(record: Record): Long

    // *************
    // **** 删除 ****
    // *************

    /**
     * 删除记录
     * @param record 要删除的记录
     * @return 成功删除的行数
     */
    @Delete
    suspend fun delete(record: Record): Int

    /**
     * 删除指定项目的所有记录
     * @param projectId 项目 ID
     */
    @Query("DELETE FROM Record WHERE projectId = :projectId")
    suspend fun deleteProjectAllRecords(projectId: Long)

    // *************
    // **** 修改 ****
    // *************

    /**
     * 更新记录
     * @param record 要更新的记录
     * @return 成功更新的行数
     */
    @Update
    suspend fun update(record: Record): Int

    // *************
    // **** 查询 ****
    // *************

    // **** 记录[Record] ****

    /**
     * 获取指定 ID 的记录
     * @param id 记录 ID
     * @return 指定 ID 的记录
     */
    @Query("SELECT * FROM Record WHERE recordId = :id")
    suspend fun get(id: Long): Record

    /**
     * 获取所有记录
     * @return 所有记录
     */
    @Query("SELECT * FROM Record ORDER BY startTime DESC")
    suspend fun getAll(): List<Record>

    /**
     * 获取所有记录
     * @return 所有记录
     */
    @Query("SELECT *, recordId FROM Record ORDER BY startTime DESC")
    fun watchAll(): Flow<List<Record>>

    /**
     * 获取记录总数
     * @return 所有记录数
     */
    @Query("SELECT COUNT(*) FROM Record")
    fun watchRecordsCount(): Flow<Int>

    /**
     * 获取指定日期范围内的记录总数
     * @param startDate 开始日期，如`20210101`
     * @param endDate 结束日期，如`20210131`
     * @return 指定日期范围内的记录总数
     */
    @Query("SELECT COUNT(*) FROM Record WHERE date>=:startDate AND date<=:endDate")
    fun watchRecordsCount(
        startDate: Int,
        endDate: Int,
    ): Flow<Int>

    /**
     * 获取指定日期的记录
     * @param date 日期，如`20210101`
     * @return 指定日期的记录
     */
    @Query("SELECT *, recordId FROM Record WHERE date=:date")
    fun watchDayRecords(date: Int): Flow<List<Record>>

    /**
     * 获取指定日期范围内的记录
     * @param startDate 开始日期，如`20210101`
     * @param endDate 结束日期，如`20210131`
     * @return 指定日期范围内的记录
     */
    @Query("SELECT *, recordId FROM Record WHERE date>=:startDate AND date<=:endDate")
    fun watchPeriodRecords(
        startDate: Int,
        endDate: Int,
    ): Flow<List<Record>>

    /**
     * 获取按日期分组的记录
     * @return 按日期分组的记录
     */
    @Query("SELECT * FROM Record ORDER BY startTime DESC")
    fun watchDaysRecords(): Flow<
        Map<
            @MapColumn(columnName = "date")
            Int,
            List<Record>,
        >,
    >

    /**
     * 获取指定项目的按日期分组的记录
     * @param projectId 项目 ID
     * @return 指定项目的按日期分组的记录
     */
    @Query("SELECT * FROM Record WHERE projectId = :projectId ORDER BY startTime DESC")
    fun watchDaysProjectRecords(
        projectId: Long,
    ): Flow<
        Map<
            @MapColumn(columnName = "date")
            Int,
            List<Record>,
        >,
    >

    // **** 项目时长[ProjectDurationEntry] ****

    /**
     * 获取每个项目的总时长
     * @return 所有每个的总时长
     */
    @Query(
        """SELECT Project.projectId AS projectId,
        SUM(endTime - startTime) AS duration
        FROM Project LEFT JOIN Record
        ON Record.projectId=Project.projectId
        GROUP BY Project.projectId
        ORDER BY duration DESC""",
    )
    fun watchProjectsDuration(): Flow<List<ProjectDurationEntry>>

    /**
     * 获取指定日期范围内的各项目时长
     * @param startDate 开始日期，如`20210101`
     * @param endDate 结束日期，如`20210131`
     * @return 指定日期范围内的各项目时长
     */
    @Query(
        """SELECT projectId,
        SUM(endTime - startTime) AS duration
        FROM Record
        WHERE date>=:startDate AND date<=:endDate AND endTime<>0
        GROUP BY projectId
        ORDER BY duration DESC""",
    )
    fun watchProjectsDuration(
        startDate: Int,
        endDate: Int,
    ): Flow<List<ProjectDurationEntry>>

    /**
     * 获取每天的各项目时长
     * @return 每天的各项目时长
     */
    @Query(
        """SELECT projectId,
        SUM(endTime - startTime) AS duration,
        date
        FROM Record
        GROUP BY projectId, date
        ORDER BY date DESC""",
    )
    fun watchDaysProjectsDuration(): Flow<
        Map<
            @MapColumn(columnName = "date")
            Int,
            List<ProjectDurationEntry>,
        >,
    >

    // **** 时长 ****

    /**
     * 获取指定日期的总时长
     * @param date 日期，如`20210101`
     * @return 指定日期的总时长
     */
    @Query("SELECT SUM(endTime - startTime) AS duration FROM Record WHERE date=:date")
    fun watchDayDuration(date: Int): Flow<Long>

    /**
     * 获取每天的总时长
     * @return 每天的总时长
     */
    @Query("SELECT date, SUM(endTime - startTime) AS duration FROM Record GROUP BY date")
    fun watchDaysDuration(): Flow<
        Map<
            @MapColumn(columnName = "date")
            Int,
            @MapColumn(columnName = "duration")
            Long,
        >,
    >

    /**
     * 获取指定项目的每天的总时长
     * @param projectId 项目 ID
     * @return 指定项目的每天的总时长
     */
    @Query(
        "SELECT date," +
            "SUM(endTime - startTime) AS duration " +
            "FROM Record " +
            "WHERE projectId = :projectId " +
            "GROUP BY date",
    )
    fun watchDaysDuration(
        projectId: Long,
    ): Flow<
        Map<
            @MapColumn(columnName = "date")
            Int,
            @MapColumn(
                columnName = "duration",
            )
            Long,
        >,
    >

    /**
     * 获取指定日期范围内的每天的总时长
     * @param startDate 开始日期，如`20210101`
     * @param endDate 结束日期，如`20210131`
     * @return 指定日期范围内的每天的总时长
     */
    @Query(
        """SELECT date,
        SUM(endTime - startTime) AS duration
        FROM Record
        WHERE date>=:startDate AND date<=:endDate
        GROUP BY date ORDER BY date DESC""",
    )
    fun watchDaysDuration(
        startDate: Int,
        endDate: Int,
    ): Flow<
        Map<
            @MapColumn(
                columnName = "date",
            )
            Int,
            @MapColumn(
                columnName = "duration",
            )
            Long,
        >,
    >

    /**
     * 获取某年每月的总时长
     * @param year 年份
     * @return 某年每月的总时长
     */
    @Query(
        """SELECT (date%10000)/100 AS month,
        SUM(endTime - startTime) AS duration
        FROM Record
        WHERE date/10000=:year
        GROUP BY month ORDER BY date DESC""",
    )
    fun watchMonthsDuration(
        year: Int,
    ): Flow<
        Map<
            @MapColumn(
                columnName = "month",
            )
            Int,
            @MapColumn(
                columnName = "duration",
            )
            Long,
        >,
    >

    /**
     * 获取每年的总时长
     * @return 每年的总时长
     */
    @Query("SELECT date/10000 AS year, SUM(endTime - startTime) AS duration FROM Record GROUP BY year ORDER BY year DESC")
    fun watchYearsDuration(): Flow<
        Map<
            @MapColumn(
                columnName = "year",
            )
            Int,
            @MapColumn(
                columnName = "duration",
            )
            Long,
        >,
    >
}
