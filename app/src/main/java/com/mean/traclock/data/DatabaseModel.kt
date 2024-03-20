package com.mean.traclock.data

import android.content.Context
import com.elvishew.xlog.XLog
import com.mean.traclock.database.AppDatabase
import com.mean.traclock.database.Project
import com.mean.traclock.database.Record
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

internal class DatabaseModel(
    mContext: Context,
) {
    private val recordDao = AppDatabase.getDatabase(mContext).recordDao()
    private val projectDao = AppDatabase.getDatabase(mContext).projectDao()

    private val scope = CoroutineScope(Dispatchers.IO)

    /** 所有的项目 */
    private val _projects =
        runBlocking(Dispatchers.IO) { projectDao.getAll().associateBy { it.id }.toMutableMap() }

    /** 所有项目 */
    val projects: ImmutableMap<Int, Project>
        get() = _projects.toImmutableMap()

    /**
     * 通过 id 获取记录
     * @param id 记录的 id
     */
    suspend fun getRecord(id: Long) = recordDao.getRecord(id)

    /**
     * 删除记录
     * @param record 要删除的记录
     */
    fun deleteRecord(record: Record) {
        scope.launch {
            recordDao.delete(record)
        }
    }

    /**
     * 删除项目（包括该项目的所有记录）
     * @param projectId 要删除的项目 id
     */
    fun deleteProject(projectId: Int) {
        _projects.remove(projectId)
        scope.launch {
            projectDao.delete(projectId)
            recordDao.deleteByProject(projectId)
        }
    }

    /**
     * 增加项目
     * @param project 要增加的项目
     */
    suspend fun insertProject(project: Project): Int {
        return if (project.name in projects.map { it.value.name }) {
            -1
        } else {
            val id = projectDao.insert(project)
            project.id = id.toInt()
            _projects[project.id] = project
            project.id
        }
    }

    /**
     * 增加记录
     * @param record 要增加的记录
     * @return 是否插入成功
     */
    fun insertRecord(record: Record): Boolean {
        if (record.project in projects) {
            scope.launch {
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
            scope.launch {
                recordDao.update(record)
            }
            return true
        }
        return false
    }

    /**
     * 更新项目（同时更新该项目的所有记录）
     * @param oldProjectId 旧项目 id
     * @param newProject 新项目
     * @return 是否更新成功
     */
    fun updateProject(
        oldProjectId: Int,
        newProject: Project,
    ): Boolean {
        if (newProject.id !in projects) {
            _projects[newProject.id] = newProject
            scope.launch {
                try {
                    projectDao.insert(newProject)
                    projectDao.delete(oldProjectId)
                    recordDao.update(oldProjectId, newProject.id)
                } catch (e: Exception) {
                    XLog.e("更新项目失败", e)
                }
            }
            return true
        } else {
            XLog.d("项目已存在：%s", newProject.name)
            return false
        }
    }

    /**
     * 更新项目颜色
     * @param project 要更新的项目
     * @return 是否更新成功
     */
    suspend fun updateProject(project: Project): Boolean {
        _projects[project.id] = project
        return projectDao.update(project) == 1
    }

    fun getProjectsTimeOfDay(date: Int) = recordDao.getProjectsTimeOfDay(date)

    fun getRecordsOfDays() = recordDao.getRecordsOfDays()

    fun getRecordsOfDays(projectId: Int) = recordDao.getRecordsOfDays(projectId)

    fun getProjectsTimeOfDays() = recordDao.getProjectsTimeOfDays()

    fun getTimeOfDays() = recordDao.getTimeOfDays()

    fun getTimeOfDays(projectId: Int) = recordDao.getTimeOfDays(projectId)

    fun getProjectsTime() = recordDao.getProjectsTime()

    suspend fun getProject(id: Int) = projectDao.getProject(id)

    suspend fun getRecords(projectId: Int) = recordDao.getRecords(projectId)
}
