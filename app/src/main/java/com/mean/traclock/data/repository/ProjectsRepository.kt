package com.mean.traclock.data.repository

import com.mean.traclock.data.Project
import com.mean.traclock.data.database.ProjectDao
import com.mean.traclock.data.database.RecordDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProjectsRepository
    @Inject
    constructor(private val projectDao: ProjectDao, private val recordDao: RecordDao) {
        suspend fun get(id: Long): Project = projectDao.get(id)

        suspend fun insert(project: Project): Long {
            val id = projectDao.insert(project)
            project.id = id
            _projects[id] = project
            return id
        }

        suspend fun update(project: Project): Int {
            _projects[project.id] = project
            val res = projectDao.update(project)
            return res
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
