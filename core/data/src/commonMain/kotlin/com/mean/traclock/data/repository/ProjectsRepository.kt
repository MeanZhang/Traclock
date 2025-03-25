package com.mean.traclock.data.repository

import com.mean.traclock.data.model.asEntry
import com.mean.traclock.database.dao.ProjectDao
import com.mean.traclock.database.dao.RecordDao
import com.mean.traclock.database.model.asExternalProject
import com.mean.traclock.model.Project
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class ProjectsRepository(private val projectDao: ProjectDao, private val recordDao: RecordDao) {
    private val scope = CoroutineScope(Dispatchers.IO)
    // **************
    // **** 增加 ****
    // **************

    /**
     * 插入一个项目（[Project]）
     * @param project 要插入的项目
     * @return 插入成功的项目的 id
     */
    suspend fun insert(project: Project): Long {
        val id =
            withContext(Dispatchers.IO) {
                projectDao.insert(project.asEntry())
            }
        return id
    }

    // **************
    // **** 删除 ****
    // **************

    /**
     * 删除一个项目（[Project]）
     * @param projectId 要删除的项目
     * @return 删除成功的项目数量
     */
    suspend fun delete(projectId: Long) {
        withContext(Dispatchers.IO) {
            recordDao.deleteProjectAllRecords(projectId)
            projectDao.delete(projectId)
        }
    }

    // **************
    // **** 修改 ****
    // **************

    /**
     * 更新一个项目（[Project]）
     * @param project 要更新的项目
     * @return 更新成功的项目数量
     */
    suspend fun update(project: Project): Int {
        return withContext(Dispatchers.IO) {
            projectDao.update(project.asEntry())
        }
    }

    // **************
    // **** 查询 ****
    // **************

    /**
     * 获取指定 ID 的项目
     * @param id 项目 ID
     * @return 指定 ID 的项目
     */
    suspend fun get(id: Long): Project =
        withContext(Dispatchers.IO) {
            projectDao.get(id).asExternalProject()
        }

    val projects: Flow<List<Project>>
        get() = projectDao.watchAll().map { it.map { entry -> entry.asExternalProject() } }
}
