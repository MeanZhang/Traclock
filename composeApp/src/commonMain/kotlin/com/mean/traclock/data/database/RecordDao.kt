package com.mean.traclock.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.MapColumn
import androidx.room.Query
import androidx.room.Update
import com.mean.traclock.data.Record
import kotlinx.coroutines.flow.Flow

@Dao
interface RecordDao {
    @Query("SELECT *, id FROM Record WHERE id = :id")
    suspend fun get(id: Long): Record

    @Query("SELECT *, id FROM Record WHERE project = :projectId")
    suspend fun getRecords(projectId: Long): List<Record>

    /**
     * 增加记录
     * @param record 要增加的记录
     * @return 插入记录的 id
     */
    @Insert
    suspend fun insert(record: Record): Long

    /**
     * 删除记录
     * @param record 要删除的记录
     * @return 成功删除的行数
     */
    @Delete
    suspend fun delete(record: Record): Int

    /**
     * 更新记录
     * @param record 要更新的记录
     * @return 成功更新的行数
     */
    @Update
    suspend fun update(record: Record): Int

    @Query("UPDATE Record SET project = :newProjectId WHERE project = :oldProjectId")
    suspend fun update(
        oldProjectId: Long,
        newProjectId: Long,
    )

    @Query("DELETE FROM Record WHERE project = :projectId")
    suspend fun deleteByProject(projectId: Long)

    @Query("SELECT *, id FROM Record ORDER BY startTime DESC")
    fun getAll(): Flow<List<Record>>

    @Query("SELECT *, id FROM Record ORDER BY startTime DESC")
    fun getRecordsOfDays(): Flow<
        Map<
            @MapColumn(columnName = "date")
            Int,
            List<Record>,
        >,
    >

    @Query("SELECT *, id FROM Record WHERE project = :projectId ORDER BY startTime DESC")
    fun getRecordsOfDays(
        projectId: Long,
    ): Flow<
        Map<
            @MapColumn(columnName = "date")
            Int,
            List<Record>,
        >,
    >

    @Query("SELECT *, id FROM Record ORDER BY startTime DESC")
    suspend fun getRecordsList(): List<Record>

    @Query(
        "SELECT Project.id AS project," +
            "0 AS startTime," +
            "SUM(endTime - startTime) AS endTime," +
            "0 AS date," +
            "0 AS id " +
            "FROM Project LEFT JOIN Record " +
            "ON Record.project=Project.id " +
            "GROUP BY Project.name " +
            "ORDER BY endTime DESC",
    )
    fun getProjectsTime(): Flow<List<Record>>

    @Query("SELECT date, SUM(endTime - startTime) AS time FROM Record GROUP BY date")
    fun getTimeOfDays(): Flow<
        Map<
            @MapColumn(columnName = "date")
            Int,
            @MapColumn(columnName = "time")
            Long,
        >,
    >

    @Query(
        "SELECT date," +
            "SUM(endTime - startTime) AS time " +
            "FROM Record " +
            "WHERE project = :projectId " +
            "GROUP BY date",
    )
    fun getTimeOfDays(
        projectId: Long,
    ): Flow<
        Map<
            @MapColumn(columnName = "date")
            Int,
            @MapColumn(
                columnName = "time",
            )
            Long,
        >,
    >

    @Query(
        "SELECT project," +
            "0 AS startTime," +
            "SUM(endTime - startTime) AS endTime," +
            "date, id " +
            "FROM Record " +
            "GROUP BY project, date " +
            "ORDER BY date DESC",
    )
    fun getProjectsTimeOfDays(): Flow<
        Map<
            @MapColumn(columnName = "date")
            Int,
            List<Record>,
        >,
    >

    /**
     * 获取指定日期的记录
     * @param date 指定日期，如`20210101`
     * @return 指定日期的记录
     */
    @Query("SELECT *, id FROM Record WHERE date=:date")
    fun getRecords(date: Int): Flow<List<Record>>

    /**
     * 获取指定日期范围内的记录
     * @param startDate 开始日期，如`20210101`
     * @param endDate 结束日期，如`20210131`
     * @return 指定日期范围内的记录
     */
    @Query("SELECT *, id FROM Record WHERE date>=:startDate AND date<=:endDate")
    fun getRecords(
        startDate: Int,
        endDate: Int,
    ): Flow<List<Record>>

    @Query(
        "SELECT project," +
            "0 AS startTime," +
            "SUM(endTime - startTime) AS endTime," +
            "date, id " +
            "FROM Record " +
            "WHERE date>=:startDate AND date<=:endDate AND endTime<>0 " +
            "GROUP BY project " +
            "ORDER BY endTime DESC",
    )
    fun getProjectsTimeOfPeriod(
        startDate: Int,
        endDate: Int,
    ): Flow<List<Record>>

    @Query("SELECT COUNT(*) FROM Record")
    fun getAllRecordsNumber(): Flow<Int>

    @Query("SELECT COUNT(*) FROM Record WHERE date>=:startDate AND date<=:endDate")
    fun getRecordsNumber(
        startDate: Int,
        endDate: Int,
    ): Flow<Int>

    /**
     * 获取所有记录
     * @return 所有记录
     */
    @Query("SELECT *, id FROM Record")
    fun getAllRecords(): Flow<List<Record>>

    /**
     * 获取每年的总时长
     * @return 每年的总时长
     */
    @Query("SELECT date/10000 AS year, SUM(endTime - startTime) AS time FROM Record GROUP BY year ORDER BY year DESC")
    fun getDurationsOfYears(): Flow<
        Map<
            @MapColumn(
                columnName = "year",
            )
            Int,
            @MapColumn(
                columnName = "time",
            )
            Long,
        >,
    >
}
