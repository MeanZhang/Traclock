package com.mean.traclock.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.mean.traclock.database.model.ProjectEntry
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
    suspend fun insert(project: ProjectEntry): Long

    // **************
    // **** 删除 ****
    // **************

    /**
     * 删除一个项目（[Project]）
     * @param projectId 要删除的项目
     * @return 删除成功的项目数量
     */
    @Query("DELETE FROM projects WHERE project_id = :projectId")
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
    suspend fun update(project: ProjectEntry): Int

    // **************
    // **** 查询 ****
    // **************

    /**
     * 获取指定 ID 的项目
     * @param id 项目 ID
     * @return 指定 ID 的项目
     */
    @Query("SELECT * FROM projects WHERE project_id = :id")
    suspend fun get(id: Long): ProjectEntry

    /**
     * 获取所有项目
     * @return 所有项目
     */
    @Query("SELECT * FROM projects")
    suspend fun getAll(): List<ProjectEntry>

    /**
     * 获取所有项目
     * @return 所有项目
     */
    @Query("SELECT * FROM projects")
    fun watchAll(): Flow<List<ProjectEntry>>
}
