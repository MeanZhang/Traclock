package com.mean.traclock.viewmodels

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import com.mean.traclock.data.DataModel
import com.mean.traclock.database.Project
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class EditProjectViewModel(private val initialName: String, private val initialColor: Color) :
    ViewModel() {
    private val _name = MutableStateFlow(initialName)
    private val _color = MutableStateFlow(initialColor)

    val name: StateFlow<String>
        get() = _name
    val color: StateFlow<Color>
        get() = _color

    fun setName(name: String) {
        _name.value = name
    }

    fun setColor(color: Color) {
        _color.value = color
    }

    fun isModified(): Boolean {
        return _name.value != initialName || _color.value != initialColor
    }

    fun updateProject(): Int {
        return when {
            _name.value != initialName -> { // 项目名发生变化
                if (DataModel.dataModel.updateProject(
                        initialName,
                        Project(_name.value, _color.value.toArgb())
                    )
                ) {
                    1
                } else {
                    -1 // 已存在
                }
            }
            _color.value != initialColor -> { // 项目名没变，颜色变化
                DataModel.dataModel.updateProject(Project(initialName, _color.value.toArgb()))
                1
            }
            else -> {
                0 // 项目信息没有变化
            }
        }
    }
}
