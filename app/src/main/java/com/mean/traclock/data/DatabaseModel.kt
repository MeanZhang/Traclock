package com.mean.traclock.data

import android.content.Context
import com.elvishew.xlog.XLog
import com.mean.traclock.database.AppDatabase
import com.mean.traclock.database.Project
import com.mean.traclock.database.Record
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlin.concurrent.thread

internal class DatabaseModel(
    mContext: Context
) {
    private val recordDao = AppDatabase.getDatabase(mContext).recordDao()
    private val projectDao = AppDatabase.getDatabase(mContext).projectDao()

    /** 所有的项目 */
    private val _projects: MutableMap<String, Int> = runBlocking(Dispatchers.IO) {
        projectDao.getAll()
            .associate { Pair(it.name, it.color) }.toMutableMap()
    }

    /** 所有项目 */
    val projects: Map<String, Int>
        get() = _projects

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
        _projects.remove(project.name)
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
        if (project.name !in projects) {
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
        if (record.project in projects) {
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
        if (record.project in projects) {
            thread {
                recordDao.update(record)
            }
            return true
        }
        return false
    }

    /**
     * 更新项目（同时更新该项目的所有记录）
     * @param oldProject 旧项目名
     * @param newProject 新项目
     * @return 是否更新成功
     */
    fun updateProject(oldProject: String, newProject: Project): Boolean {
        _projects[newProject.name] = newProject.color
        if (newProject.name !in projects) {
            thread {
                try {
                    projectDao.insert(newProject)
                    if (oldProject.isNotBlank()) {
                        projectDao.delete(oldProject)
                        recordDao.update(oldProject, newProject.name)
                    }
                } catch (e: Exception) {
                    XLog.e("更新项目失败", e)
                }
            }
            return true
        }
        XLog.d("项目已存在：%s", newProject.name)
        return false
    }

    /**
     * 更新项目颜色
     * @param project 要更新的项目
     */
    fun updateProject(project: Project) {
        _projects[project.name] = project.color
        thread {
            projectDao.update(project)
        }
    }

    fun getProjectsTimeOfDay(date: Int) = recordDao.getProjectsTimeOfDay(date)
    fun getRecordsOfDays() = recordDao.getRecordsOfDays()
    fun getProjectsTimeOfDays() = recordDao.getProjectsTimeOfDays()
    fun getTimeOfDays() = recordDao.getTimeOfDays()
    fun getProjectsTime() = recordDao.getProjectsTime()
    fun getRecordsOfDays(projectName: String) = recordDao.getRecordsOfDays(projectName)
    fun getTimeOfDays(projectName: String) = recordDao.getTimeOfDays(projectName)
}
