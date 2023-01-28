package com.mean.traclock.viewmodels

import androidx.lifecycle.ViewModel
import com.mean.traclock.data.DataModel
import com.mean.traclock.database.Record
import com.mean.traclock.utils.TimeUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

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
            return if (_project.value.isBlank()) {
                -1 // 项目名为空
            } else {
                if (_project.value in DataModel.dataModel.projects) {
                    record.project = _project.value
                    record.startTime = startTime.value
                    record.endTime = endTime.value
                    record.date = TimeUtils.getIntDate(record.startTime)
                    DataModel.dataModel.updateRecord(record)
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
