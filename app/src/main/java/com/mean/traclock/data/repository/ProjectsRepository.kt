package com.mean.traclock.data.repository

import com.mean.traclock.data.Project
import com.mean.traclock.data.database.ProjectDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProjectsRepository
    @Inject
    constructor(private val projectDao: ProjectDao) {
        suspend fun get(id: Long): Project = projectDao.get(id)

        suspend fun insert(project: Project): Long {
            val res = projectDao.insert(project)
            _projects[project.id] = project
            return res
        }

        suspend fun update(project: Project): Int {
            val res = projectDao.update(project)
            _projects[project.id] = project
            return res
        }

        fun delete(id: Long) {
            projectDao.delete(id)
            _projects.remove(id)
        }

        private val _projects: MutableMap<Long, Project> =
            runBlocking(Dispatchers.IO) {
                projectDao.getAll().associateBy { it.id }
            }.toMutableMap()

        val projects: Map<Long, Project>
            get() = _projects.toMap()
    }
