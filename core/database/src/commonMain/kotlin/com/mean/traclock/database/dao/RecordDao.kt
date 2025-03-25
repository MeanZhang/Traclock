package com.mean.traclock.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.MapColumn
import androidx.room.Query
import androidx.room.Update
import com.mean.traclock.database.model.ProjectDurationEntry
import com.mean.traclock.database.model.RecordEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface RecordDao {
    // **************
    // **** 增加 ****
    // **************

    /**
     * 增加记录
     * @param record 要增加的记录
     * @return 插入记录的 id
     */
    @Insert
    suspend fun insert(record: RecordEntry): Long

    // **************
    // **** 删除 ****
    // **************

    /**
     * 删除记录
     * @param record 要删除的记录
     * @return 成功删除的行数
     */
    @Delete
    suspend fun delete(record: RecordEntry): Int

    /**
     * 删除指定项目的所有记录
     * @param projectId 项目 ID
     */
    @Query("DELETE FROM records WHERE project = :projectId")
    suspend fun deleteProjectAllRecords(projectId: Long)

    // **************
    // **** 修改 ****
    // **************

    /**
     * 更新记录
     * @param record 要更新的记录
     * @return 成功更新的行数
     */
    @Update
    suspend fun update(record: RecordEntry): Int

    // **************
    // **** 查询 ****
    // **************

    // **** 记录[Record] ****

    /**
     * 获取指定 ID 的记录
     * @param id 记录 ID
     * @return 指定 ID 的记录
     */
    @Query("SELECT * FROM records WHERE record_id = :id")
    suspend fun get(id: Long): RecordEntry

    /**
     * 获取所有记录
     * @return 所有记录
     */
    @Query("SELECT * FROM records ORDER BY start_time DESC")
    suspend fun getAll(): List<RecordEntry>

    /**
     * 获取所有记录
     * @return 所有记录
     */
    @Query("SELECT * FROM records ORDER BY start_time DESC")
    fun watchAll(): Flow<List<RecordEntry>>

    /**
     * 获取记录总数
     * @return 所有记录数
     */
    @Query("SELECT COUNT(*) FROM records")
    fun watchRecordsCount(): Flow<Int>

    /**
     * 获取指定日期范围内的记录总数
     * @param startDate 开始日期，如`20210101`
     * @param endDate 结束日期，如`20210131`
     * @return 指定日期范围内的记录总数
     */
    @Query("SELECT COUNT(*) FROM records WHERE date>=:startDate AND date<=:endDate")
    fun watchRecordsCount(
        startDate: Int,
        endDate: Int,
    ): Flow<Int>

    /**
     * 获取指定日期的记录
     * @param date 日期，如`20210101`
     * @return 指定日期的记录
     */
    @Query("SELECT * FROM records WHERE date=:date")
    fun watchDayRecords(date: Int): Flow<List<RecordEntry>>

    /**
     * 获取指定日期范围内的记录
     * @param startDate 开始日期，如`20210101`
     * @param endDate 结束日期，如`20210131`
     * @return 指定日期范围内的记录
     */
    @Query("SELECT * FROM records WHERE date>=:startDate AND date<=:endDate")
    fun watchPeriodRecords(
        startDate: Int,
        endDate: Int,
    ): Flow<List<RecordEntry>>

    /**
     * 获取按日期分组的记录
     * @return 按日期分组的记录
     */
    @Query("SELECT * FROM records ORDER BY start_time DESC")
    fun watchDaysRecords(): Flow<
        Map<
            @MapColumn(columnName = "date")
            Int,
            List<RecordEntry>,
        >,
    >

    /**
     * 获取指定项目的按日期分组的记录
     * @param projectId 项目 ID
     * @return 指定项目的按日期分组的记录
     */
    @Query("SELECT * FROM records WHERE project = :projectId ORDER BY start_time DESC")
    fun watchDaysProjectRecords(
        projectId: Long,
    ): Flow<
        Map<
            @MapColumn(columnName = "date")
            Int,
            List<RecordEntry>,
        >,
    >

    // **** 项目时长[ProjectDurationEntry] ****

    /**
     * 获取每个项目的总时长
     * @return 所有每个的总时长
     */
    @Query(
        """SELECT projects.project_id AS project_id, projects.name AS name, projects.color AS color,
        SUM(end_time - start_time) AS duration
        FROM projects LEFT JOIN records
        ON records.project=projects.project_id
        GROUP BY project_id
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
        """SELECT projects.project_id AS project_id, projects.name AS name, projects.color AS color,
        SUM(end_time - start_time) AS duration
        FROM records LEFT JOIN projects
        ON records.project=projects.project_id
        WHERE date>=:startDate AND date<=:endDate AND end_time<>0
        GROUP BY project_id
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
        """SELECT projects.project_id AS project_id, projects.name AS name, projects.color AS color,
        SUM(end_time - start_time) AS duration,
        date
        FROM records LEFT JOIN projects
        ON records.project=projects.project_id
        GROUP BY project_id, date
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
    @Query("SELECT SUM(end_time - start_time) AS duration FROM records WHERE date=:date")
    fun watchDayDuration(date: Int): Flow<Long>

    /**
     * 获取每天的总时长
     * @return 每天的总时长
     */
    @Query("SELECT date, SUM(end_time - start_time) AS duration FROM records GROUP BY date")
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
            "SUM(end_time - start_time) AS duration " +
            "FROM records " +
            "WHERE project = :projectId " +
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
        SUM(end_time - start_time) AS duration
        FROM records
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
        SUM(end_time - start_time) AS duration
        FROM records
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
    @Query("SELECT date/10000 AS year, SUM(end_time - start_time) AS duration FROM records GROUP BY year ORDER BY year DESC")
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
