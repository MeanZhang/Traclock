package com.mean.traclock.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ProjectDao {
    @Query("SELECT *, rowid FROM Project WHERE rowid = :id")
    suspend fun getProject(id: Int): Project

    @Insert
    suspend fun insert(project: Project): Long

    @Delete
    fun delete(project: Project)

    @Query("DELETE FROM Project WHERE rowid = :id")
    fun delete(id: Int)

    @Update
    suspend fun update(project: Project): Int

    @Query("SELECT *, rowid FROM Project")
    fun getAll(): List<Project>

    @Query("SELECT *, rowid FROM Project WHERE name LIKE :name")
    fun findByName(name: String): Project
}
