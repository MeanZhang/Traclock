package com.mean.traclock.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import data.Project
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectDao {
    @Query("SELECT *, id FROM Project WHERE id = :id")
    suspend fun get(id: Long): Project

    /**
     * 插入一个项目（[Project]）
     * @param project 要插入的项目
     * @return 插入成功的项目的 id
     */
    @Insert
    suspend fun insert(project: Project): Long

    /**
     * 删除一个项目（[Project]）
     * @param project 要删除的项目
     * @return 删除成功的项目数量
     */
    @Delete
    suspend fun delete(project: Project): Int

    @Query("DELETE FROM Project WHERE id = :id")
    suspend fun delete(id: Long): Int

    @Update
    suspend fun update(project: Project): Int

    @Query("SELECT *, id FROM Project")
    suspend fun getAll(): List<Project>

    @Query("SELECT *, id FROM Project WHERE name LIKE :name")
    suspend fun findByName(name: String): Project

    @Query("SELECT *, id FROM Project")
    fun getAllFlow(): Flow<List<Project>>
}
