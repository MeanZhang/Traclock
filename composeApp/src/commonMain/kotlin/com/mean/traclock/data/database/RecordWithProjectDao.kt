package com.mean.traclock.data.database

import androidx.room.Dao
import androidx.room.MapColumn
import androidx.room.Query
import androidx.room.Transaction
import com.mean.traclock.model.RecordWithProject
import kotlinx.coroutines.flow.Flow

@Dao
interface RecordWithProjectDao {
    /**
     * 获取指定 ID 的详细记录
     * @param id 记录 ID
     * @return 指定 ID 的详细记录
     */
    @Transaction
    @Query("SELECT * FROM Record LEFT JOIN Project ON Record.project = Project.project_id WHERE Record.record_id = :id")
    suspend fun get(id: Long): RecordWithProject

    /**
     * 获取指定日期的详细记录
     * @param date 日期，如`20210101`
     * @return 指定日期的详细记录
     */
    @Transaction
    @Query("SELECT * FROM Record LEFT JOIN Project ON Record.project = Project.project_id WHERE Record.date = :date")
    fun watchDayRecordsWithProject(date: Int): Flow<List<RecordWithProject>>

    /**
     * 获取按日期分组的详细记录
     * @return 按日期分组的详细记录
     */
    @Transaction
    @Query("SELECT * FROM Record LEFT JOIN Project ON Record.project = Project.project_id ORDER BY start_time DESC")
    fun watchDaysRecordsWithProject(): Flow<
        Map<
            @MapColumn(columnName = "date")
            Int,
            List<RecordWithProject>,
        >,
    >

    /**
     * 获取所有详细记录
     * @return 所有详细记录
     */
    @Transaction
    @Query("SELECT * FROM Record LEFT JOIN Project ON Record.project = Project.project_id")
    suspend fun getAll(): List<RecordWithProject>
}
