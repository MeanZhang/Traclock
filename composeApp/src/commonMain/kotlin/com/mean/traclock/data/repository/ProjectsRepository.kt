package com.mean.traclock.data.repository

import com.mean.traclock.data.database.ProjectDao
import com.mean.traclock.data.database.RecordDao
import data.Project
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.lang.IllegalArgumentException

class ProjectsRepository(private val projectDao: ProjectDao, private val recordDao: RecordDao) {
    suspend fun get(id: Long): Project =
        withContext(Dispatchers.IO) {
            projectDao.get(id)
        }

    suspend fun insert(project: Project): Long {
        if (projects.any { it.value.name == project.name }) throw IllegalArgumentException("项目名${project.name}已存在")
        val id =
            withContext(Dispatchers.IO) {
                projectDao.insert(project)
            }
        project.id = id
        _projects[id] = project
        return id
    }

    suspend fun update(project: Project): Int {
        _projects[project.id] = project
        return withContext(Dispatchers.IO) {
            projectDao.update(project)
        }
    }

    suspend fun delete(id: Long) {
        _projects.remove(id)
        withContext(Dispatchers.IO) {
            recordDao.deleteByProject(id)
            projectDao.delete(id)
        }
    }

    private val _projects: MutableMap<Long, Project> =
        runBlocking(Dispatchers.IO) {
            projectDao.getAll().associateBy { it.id }
        }.toMutableMap()

    val projects: Map<Long, Project>
        get() = _projects
}
