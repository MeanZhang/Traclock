package com.mean.traclock.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mean.traclock.data.DataModel
import com.mean.traclock.database.Record
import com.mean.traclock.utils.TimeUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditRecordViewModel
    @Inject
    constructor(savedStateHandle: SavedStateHandle) : ViewModel() {
        private lateinit var record: Record
        private val _projectId: MutableStateFlow<Int?> = MutableStateFlow(null)
        private val _startTime = MutableStateFlow(0L)
        private val _endTime = MutableStateFlow(0L)
        val projectId: StateFlow<Int?>
            get() = _projectId
        val startTime: StateFlow<Long>
            get() = _startTime
        val endTime: StateFlow<Long>
            get() = _endTime

        init {
            viewModelScope.launch {
                record = DataModel.dataModel.getRecord(savedStateHandle.get<Long>("id")!!)
                _projectId.value = record.project
                _startTime.value = record.startTime
                _endTime.value = record.endTime
            }
        }

        val isModified: Boolean
            get() =
                if (::record.isInitialized.not()) {
                    false
                } else {
                    _projectId.value != record.project || _startTime.value != record.startTime || _endTime.value != record.endTime
                }

        fun updateRecord(): Int {
            if (isModified) {
                return if (_projectId.value == null) {
                    -1 // 项目名为空
                } else {
                    if (_projectId.value in DataModel.dataModel.projects) {
                        record.project = _projectId.value!!
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

        fun deleteRecord() {
            DataModel.dataModel.deleteRecord(record)
        }

        fun setProject(projectId: Int) {
            _projectId.value = projectId
        }

        fun setStartTime(startTime: Long) {
            _startTime.value = startTime
        }

        fun setEndTime(endTime: Long) {
            _endTime.value = endTime
        }
    }
