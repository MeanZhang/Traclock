package com.mean.traclock.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ProjectDao {
    @Insert
    fun insert(project: Project)

    @Delete
    fun delete(project: Project)

    @Update
    fun update(project: Project)

    @Query("SELECT * FROM Project")
    fun getAll(): LiveData<List<Project>>

    @Query("SELECT * FROM Project WHERE name LIKE :name")
    fun findByName(name: String): Project
}