package com.mean.traclock.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectDao {
    @Insert
    fun insert(project: Project)

    @Delete
    fun delete(project: Project)

    @Update
    fun update(project: Project)

    @Query("SELECT * FROM Project")
    fun getAll(): Flow<List<Project>>

    @Query("SELECT * FROM Project WHERE name LIKE :name")
    fun findByName(name: String): Project
}