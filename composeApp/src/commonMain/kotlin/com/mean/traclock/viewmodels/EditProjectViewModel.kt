package com.mean.traclock.viewmodels

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.mean.traclock.data.repository.ProjectsRepository
import com.mean.traclock.model.Project
import com.mean.traclock.ui.ProjectColor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EditProjectViewModel(
    private val projectsRepo: ProjectsRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private var project: Project? = null
    private val _name = MutableStateFlow("")
    private val _color = MutableStateFlow(ProjectColor.entries[0].color)

    val name: StateFlow<String>
        get() = _name
    val color: StateFlow<Color>
        get() = _color

    init {
        val id = savedStateHandle.get<String?>("id")?.toLong()
        viewModelScope.launch(Dispatchers.IO) {
            if (id != null) {
                project = projectsRepo.get(id)
                _name.value = project!!.name
                _color.value = project!!.color
            }
        }
    }

    fun setName(name: String) {
        _name.value = name
    }

    fun setColor(color: Color) {
        _color.value = color
    }

    val isModified: Boolean
        get() =
            if (project == null) {
                _name.value.isNotBlank()
            } else {
                _name.value != project?.name || _color.value != project?.color
            }

    suspend fun updateProject(): Int {
        if (project == null) {
            val project = Project(_name.value, _color.value)
            try {
                projectsRepo.insert(project)
            } catch (_: Exception) {
                Logger.e { "项目${_name.value}已存在" }
                return -1
            }
            Logger.d("插入项目：$project")
            return 1
        }
        return when {
            _name.value != project!!.name -> { // 项目名发生变化
                val res =
                    projectsRepo.update(
                        project!!.apply {
                            name = _name.value
                            color = _color.value
                        },
                    )
                if (res > 0) {
                    Logger.d("项目更新成功")
                }
                res
            }

            _color.value != project!!.color -> { // 项目名没变，颜色变化
                projectsRepo.update(project!!.apply { color = _color.value })
            }

            else -> {
                0 // 项目信息没有变化
            }
        }
    }
}
