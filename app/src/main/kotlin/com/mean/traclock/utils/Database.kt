package com.mean.traclock.utils

import com.mean.traclock.App
import com.mean.traclock.database.AppDatabase
import com.mean.traclock.database.Project
import com.mean.traclock.database.Record
import com.orhanobut.logger.Logger
import kotlin.concurrent.thread

/**
 * 数据库的相关操作
 */
object Database {
    private val recordDao = AppDatabase.getDatabase(App.context).recordDao()
    private val projectDao = AppDatabase.getDatabase(App.context).projectDao()
    private val projectsList = App.projects

    /**
     * 删除记录
     * @param record 要删除的记录
     */
    fun deleteRecord(record: Record) {
        thread {
            recordDao.delete(record)
        }
    }

    /**
     * 删除项目（包括该项目的所有记录）
     * @param project 要删除的项目
     */
    fun deleteProject(project: Project) {
        projectsList.remove(project.name)
        thread {
            projectDao.delete(project)
            recordDao.deleteByProject(project.name)
        }
    }

    /**
     * 增加项目
     * @param project 要增加的项目
     */
    fun insertProject(project: Project) {
        if (project.name !in projectsList) {
            projectsList[project.name] = project.color
            thread {
                projectDao.insert(project)
            }
        }
    }

    /**
     * 增加记录
     * @param record 要增加的记录
     * @return 是否插入成功
     */
    fun insertRecord(record: Record): Boolean {
        if (record.project in projectsList) {
            thread {
                recordDao.insert(record)
            }
            return true
        }
        return false
    }

    /**
     * 更新记录
     * @param record 要更新的记录
     * @return 是否更新成功
     */
    fun updateRecord(record: Record): Boolean {
        if (record.project in projectsList) {
            thread {
                recordDao.update(record)
            }
            return true
        }
        return false
    }

    /**
     * 更新项目（同时更新该项目的所有记录）
     * @param oldProject 旧项目
     * @param newProject 新项目
     * @return 是否更新成功
     */
    fun updateProject(oldProject: String, newProject: Project): Boolean {
        if (newProject.name !in projectsList) {
            projectsList.remove(oldProject)
            projectsList[newProject.name] = newProject.color
            thread {
                try {
                    projectDao.insert(newProject)
                    if (oldProject.isNotBlank()) {
                        projectDao.delete(oldProject)
                        recordDao.update(oldProject, newProject.name)
                    }
                } catch (e: Exception) {
                    Logger.e(e, "更新项目失败")
                }
            }
            return true
        }
        Logger.d("项目已存在：%s", newProject.name)
        return false
    }

    /**
     * 更新项目颜色
     * @param project 要更新的项目
     */
    fun updateProject(project: Project) {
        projectsList[project.name] = project.color
        thread {
            projectDao.update(project)
        }
    }
}
