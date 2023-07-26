package com.mean.traclock.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elvishew.xlog.XLog
import com.mean.traclock.data.DataModel
import com.mean.traclock.database.Project
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProjectViewModel @Inject constructor(savedStateHandle: SavedStateHandle) : ViewModel() {
    private var project: Project? = null
    private val _name = MutableStateFlow("")
    private val _color = MutableStateFlow(0)

    val name: StateFlow<String>
        get() = _name
    val color: StateFlow<Int>
        get() = _color

    init {
        viewModelScope.launch {
            if (savedStateHandle.get<Boolean>("isNew") == false) {
                project = DataModel.dataModel.getProject(savedStateHandle.get<Int>("id")!!)
                _name.value = project!!.name
                _color.value = project!!.color
            }
        }
    }

    fun setName(name: String) {
        _name.value = name
    }

    fun setColor(color: Int) {
        _color.value = color
    }

    val isModified: Boolean
        get() = if (project == null) {
            _name.value.isNotBlank()
        } else {
            _name.value != project?.name || _color.value != project?.color
        }

    suspend fun updateProject(): Int {
        if (project == null) {
            val project = Project(_name.value, _color.value)
            XLog.d(project)
            return if (DataModel.dataModel.insertProject(project) != -1) {
                XLog.d("项目插入成功")
                1
            } else {
                XLog.d("项目插入失败")
                -1
            }
        }
        return when {
            _name.value != project!!.name -> { // 项目名发生变化
                val res = DataModel.dataModel.updateProject(
                    project!!.apply { name = _name.value; color = _color.value },
                )
                if (res) {
                    XLog.d("项目更新成功")
                    1
                } else {
                    -1 // 已存在
                }
            }

            _color.value != project!!.color -> { // 项目名没变，颜色变化
                DataModel.dataModel.updateProject(project!!.apply { color = _color.value })
                1
            }

            else -> {
                0 // 项目信息没有变化
            }
        }
    }
}
