package com.mean.traclock.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.mean.traclock.data.Project

@Dao
interface ProjectDao {
    @Query("SELECT *, rowid FROM Project WHERE rowid = :id")
    suspend fun get(id: Long): Project

    /**
     * 插入一个项目（[Project]）
     * @param project 要插入的项目
     * @return 插入成功的项目的 rowid
     */
    @Insert
    suspend fun insert(project: Project): Long

    /**
     * 删除一个项目（[Project]）
     * @param project 要删除的项目
     * @return 删除成功的项目数量
     */
    @Delete
    fun delete(project: Project): Int

    @Query("DELETE FROM Project WHERE rowid = :id")
    fun delete(id: Long): Int

    @Update
    suspend fun update(project: Project): Int

    @Query("SELECT *, rowid FROM Project")
    suspend fun getAll(): List<Project>

    @Query("SELECT *, rowid FROM Project WHERE name LIKE :name")
    fun findByName(name: String): Project
}
