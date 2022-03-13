package com.mean.traclock.utils

import com.mean.traclock.App
import com.mean.traclock.database.AppDatabase
import com.mean.traclock.database.Project
import com.mean.traclock.database.Record
import kotlin.concurrent.thread

object Database {
    private val recordDao = AppDatabase.getDatabase(App.context).recordDao()
    private val projectDao = AppDatabase.getDatabase(App.context).projectDao()
    private val projectsList = App.projectsList
    fun deleteRecord(record: Record) {
        thread {
            recordDao.delete(record)
        }
    }

    fun deleteProject(project: Project) {
        projectsList.remove(project.name)
        thread {
            projectDao.delete(project)
            recordDao.deleteByProject(project.name)
        }
    }

    fun insertProject(project: Project) {
        if (project.name !in projectsList) {
            projectsList[project.name] = project.color
            thread {
                projectDao.insert(project)
            }
        }
    }

    fun insertRecord(record: Record): Boolean {
        if (record.project in projectsList) {
            thread {
                recordDao.insert(record)
            }
            return true
        }
        return false
    }

    fun updateRecord(record: Record): Boolean {
        if (record.project in projectsList) {
            thread {
                recordDao.update(record)
            }
            return true
        }
        return false
    }

    fun updateProject(oldProject: String, newProject: Project): Boolean {
        if (newProject.name !in projectsList) {
            projectsList.remove(oldProject)
            projectsList[newProject.name] = newProject.color
            thread {
                projectDao.delete(oldProject)
                projectDao.insert(newProject)
                recordDao.update(oldProject, newProject.name)
            }
            return true
        }
        return false
    }

    fun updateProject(project: Project) {
        projectsList[project.name] = project.color
        thread {
            projectDao.update(project)
        }
    }
}
