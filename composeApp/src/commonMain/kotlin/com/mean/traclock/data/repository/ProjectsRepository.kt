package com.mean.traclock.data.repository

import com.mean.traclock.data.database.ProjectDao
import com.mean.traclock.data.database.RecordDao
import com.mean.traclock.model.Project
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.lang.IllegalArgumentException

class ProjectsRepository(private val projectDao: ProjectDao, private val recordDao: RecordDao) {
    // *************
    // **** 增加 ****
    // *************

    /**
     * 插入一个项目（[Project]）
     * @param project 要插入的项目
     * @return 插入成功的项目的 id
     */
    suspend fun insert(project: Project): Long {
        if (projects.any { it.value.name == project.name }) throw IllegalArgumentException("项目名${project.name}已存在")
        val id =
            withContext(Dispatchers.IO) {
                projectDao.insert(project)
            }
        project.projectId = id
        _projects[id] = project
        return id
    }

    // *************
    // **** 删除 ****
    // *************

    /**
     * 删除一个项目（[Project]）
     * @param projectId 要删除的项目
     * @return 删除成功的项目数量
     */
    suspend fun delete(projectId: Long) {
        _projects.remove(projectId)
        withContext(Dispatchers.IO) {
            recordDao.deleteProjectAllRecords(projectId)
            projectDao.delete(projectId)
        }
    }

    // *************
    // **** 修改 ****
    // *************

    /**
     * 更新一个项目（[Project]）
     * @param project 要更新的项目
     * @return 更新成功的项目数量
     */
    suspend fun update(project: Project): Int {
        _projects[project.projectId] = project
        return withContext(Dispatchers.IO) {
            projectDao.update(project)
        }
    }

    // *************
    // **** 查询 ****
    // *************

    /**
     * 获取指定 ID 的项目
     * @param id 项目 ID
     * @return 指定 ID 的项目
     */
    suspend fun get(id: Long): Project =
        withContext(Dispatchers.IO) {
            projectDao.get(id)
        }

    private val _projects: MutableMap<Long, Project> =
        runBlocking(Dispatchers.IO) {
            projectDao.getAll().associateBy { it.projectId }
        }.toMutableMap()

    /** 所有项目 */
    val projects: Map<Long, Project>
        get() = _projects
}
