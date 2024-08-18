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
            "SUM(endTime - startTime)/1000 AS endTime," +
            "0 AS date," +
            "0 AS id " +
            "FROM Project LEFT JOIN Record " +
            "ON Record.project=Project.id " +
            "GROUP BY Project.name",
    )
    fun getProjectsTime(): Flow<List<Record>>

    @Query("SELECT date, SUM(endTime - startTime)/1000 AS time FROM Record GROUP BY date")
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
            "SUM(endTime - startTime)/1000 AS time " +
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
            "SUM(endTime - startTime)/1000 AS endTime," +
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

    @Query("SELECT *, id FROM Record WHERE date=:date")
    fun getRecordsOfDay(date: Int): Flow<List<Record>>

    @Query(
        "SELECT project," +
            "0 AS startTime," +
            "SUM(endTime - startTime) AS endTime," +
            "date, id " +
            "FROM Record " +
            "WHERE date=:date " +
            "GROUP BY project",
    )
    fun getProjectsTimeOfDay(date: Int): Flow<List<Record>>
}
