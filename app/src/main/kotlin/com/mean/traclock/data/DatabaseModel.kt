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
    private val mProjects: MutableMap<String, Project> = runBlocking(Dispatchers.IO) {
        projectDao.getAll()
            .associateBy { it.name }
            .toMutableMap()
    }

    val projects: Map<String, Project>
        get() = mProjects

    fun removeProject(projectName: String) {
        mProjects.remove(projectName)
    }

    fun addProject(project: Project) {
        mProjects[project.name] = project
    }

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
        DataModel.dataModel.removeProject(project.name)
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
            DataModel.dataModel.addProject(project)
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
        if (newProject.name !in projects) {
            DataModel.dataModel.removeProject(oldProject)
            DataModel.dataModel.addProject(newProject)
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
        DataModel.dataModel.addProject(project)
        thread {
            projectDao.update(project)
        }
    }
}
