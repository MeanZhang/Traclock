package com.mean.traclock.viewmodels

import androidx.lifecycle.ViewModel
import com.mean.traclock.App
import com.mean.traclock.database.AppDatabase
import com.mean.traclock.database.Record
import com.mean.traclock.utils.getIntDate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.concurrent.thread

class EditRecordViewModel(val record: Record) : ViewModel() {
    private val _project = MutableStateFlow(record.project)
    private val _startTime = MutableStateFlow(record.startTime)
    private val _endTime = MutableStateFlow(record.endTime)

    val project: StateFlow<String>
        get() = _project
    val startTime: StateFlow<Long>
        get() = _startTime
    val endTime: StateFlow<Long>
        get() = _endTime

    fun isModified(): Boolean {
        return _project.value != record.project || _startTime.value != record.startTime || _endTime.value != record.endTime
    }

    fun updateRecord(): Int {
        if (isModified()) {
            if (_project.value.isBlank()) {
                return -1 // 项目名为空
            } else {
                return if (_project.value in App.projectsList) {
                    record.project = _project.value
                    record.startTime = startTime.value
                    record.endTime = endTime.value
                    record.date = getIntDate(record.startTime)
                    thread {
                        AppDatabase.getDatabase(App.context)
                            .recordDao().update(record)
                    }
                    1
                } else {
                    -2 // 项目不存在
                }
            }
        } else {
            return 2 // 没有变化
        }
    }

    fun setProject(project: String) {
        _project.value = project
    }

    fun setStartTime(startTime: Long) {
        _startTime.value = startTime
    }

    fun setEndTime(endTime: Long) {
        _endTime.value = endTime
    }
}
