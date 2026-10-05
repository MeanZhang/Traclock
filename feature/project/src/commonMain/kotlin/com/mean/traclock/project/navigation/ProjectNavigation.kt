package com.mean.traclock.project.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.mean.traclock.project.ui.EditProject
import com.mean.traclock.project.ui.Project

/** 新建项目时使用的占位 id，与 [EditProjectKey.id] 为 null 等价 */
const val NEW_PROJECT_ID = 0L

data class ProjectKey(val id: Long) : NavKey

data class EditProjectKey(val id: Long? = null) : NavKey

fun EntryProviderScope<NavKey>.projectEntries(
    navBack: () -> Unit,
    navToProject: (Long) -> Unit,
    navToEditProject: (Long) -> Unit,
    navToEditRecord: (Long) -> Unit,
) {
    entry<ProjectKey> { key ->
        Project(
            id = key.id,
            navToProject = navToProject,
            navToEditProject = navToEditProject,
            navToEditRecord = navToEditRecord,
            navBack = navBack,
        )
    }
    entry<EditProjectKey> { key ->
        EditProject(
            id = key.id ?: NEW_PROJECT_ID,
            navBack = navBack,
        )
    }
}
