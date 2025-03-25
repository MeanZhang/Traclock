package com.mean.traclock.database.dao

import androidx.room.Dao
import androidx.room.MapColumn
import androidx.room.Query
import androidx.room.Transaction
import com.mean.traclock.database.model.RecordWithProjectEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface RecordWithProjectDao {
    /**
     * 获取指定 ID 的详细记录
     * @param id 记录 ID
     * @return 指定 ID 的详细记录
     */
    @Transaction
    @Query("SELECT * FROM records LEFT JOIN projects ON records.project = projects.project_id WHERE records.record_id = :id")
    suspend fun get(id: Long): RecordWithProjectEntry

    /**
     * 获取指定日期的详细记录
     * @param date 日期，如`20210101`
     * @return 指定日期的详细记录
     */
    @Transaction
    @Query("SELECT * FROM records LEFT JOIN projects ON records.project = projects.project_id WHERE records.date = :date")
    fun watchDayRecordsWithProject(date: Int): Flow<List<RecordWithProjectEntry>>

    /**
     * 获取按日期分组的详细记录
     * @return 按日期分组的详细记录
     */
    @Transaction
    @Query("SELECT * FROM records LEFT JOIN projects ON records.project = projects.project_id ORDER BY start_time DESC")
    fun watchDaysRecordsWithProject(): Flow<
        Map<
            @MapColumn(columnName = "date")
            Int,
            List<RecordWithProjectEntry>,
        >,
    >

    /**
     * 获取所有详细记录
     * @return 所有详细记录
     */
    @Transaction
    @Query("SELECT * FROM records LEFT JOIN projects ON records.project = projects.project_id")
    suspend fun getAll(): List<RecordWithProjectEntry>
}
