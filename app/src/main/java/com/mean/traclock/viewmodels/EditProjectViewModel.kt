package com.mean.traclock.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mean.traclock.App
import com.mean.traclock.database.AppDatabase
import com.mean.traclock.database.Project
import kotlin.concurrent.thread

class EditProjectViewModel(private val initialName: String, private val initialColor: Int) :
    ViewModel() {
    private val _name = MutableLiveData(initialName)
    private val _color = MutableLiveData(initialColor)

    val name: LiveData<String>
        get() = _name
    val color: LiveData<Int>
        get() = _color

    fun setName(name: String) {
        _name.value = name
    }

    fun setColor(color: Int) {
        _color.value = color
    }

    fun isModified(): Boolean {
        return _name.value != initialName || _color.value != initialColor
    }

    fun updateProject(): Int {
        return when {
            _name.value != initialName && _name.value in App.projectsList -> {
                -1//项目已存在
            }
            _name.value != initialName -> {//项目名发生变化
                thread {
                    AppDatabase.getDatabase(App.context).projectDao().let {
                        it.insert(Project(_name.value ?: "", _color.value ?: 0))
                        it.delete(Project(initialName, initialColor))
                    }
                    _name.value?.let {
                        AppDatabase.getDatabase(App.context).recordDao().updateProject(
                            initialName,
                            it
                        )
                    }
                }
                1
            }
            _color.value != initialColor -> {//项目名没变，颜色变化
                thread {
                    AppDatabase.getDatabase(App.context).projectDao()
                        .update(Project(initialName, _color.value ?: 0))
                }
                1
            }
            else -> {
                0//项目信息没有变化
            }
        }
    }
}