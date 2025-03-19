package com.mean.traclock.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.mean.traclock.model.Project
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectDao {
    // *************
    // **** 增加 ****
    // *************

    /**
     * 插入一个项目（[Project]）
     * @param project 要插入的项目
     * @return 插入成功的项目的 id
     */
    @Insert
    suspend fun insert(project: Project): Long

    // **************
    // **** 删除 ****
    // **************

    /**
     * 删除一个项目（[Project]）
     * @param projectId 要删除的项目
     * @return 删除成功的项目数量
     */
    @Query("DELETE FROM Project WHERE project_id = :projectId")
    suspend fun delete(projectId: Long): Int

    // **************
    // **** 修改 ****
    // **************

    /**
     * 更新一个项目（[Project]）
     * @param project 要更新的项目
     * @return 更新成功的项目数量
     */
    @Update
    suspend fun update(project: Project): Int

    // **************
    // **** 查询 ****
    // **************

    /**
     * 获取指定 ID 的项目
     * @param id 项目 ID
     * @return 指定 ID 的项目
     */
    @Query("SELECT * FROM Project WHERE project_id = :id")
    suspend fun get(id: Long): Project

    /**
     * 获取所有项目
     * @return 所有项目
     */
    @Query("SELECT * FROM Project")
    suspend fun getAll(): List<Project>

    /**
     * 获取所有项目
     * @return 所有项目
     */
    @Query("SELECT * FROM Project")
    fun watchAll(): Flow<List<Project>>
}
