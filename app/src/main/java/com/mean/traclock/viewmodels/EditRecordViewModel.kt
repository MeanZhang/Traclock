package com.mean.traclock.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mean.traclock.data.Record
import com.mean.traclock.data.repository.ProjectsRepository
import com.mean.traclock.data.repository.RecordsRepository
import com.mean.traclock.utils.TimeUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditRecordViewModel
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle,
        private val recordsRepo: RecordsRepository,
        projectsRepository: ProjectsRepository,
    ) : ViewModel() {
        private lateinit var record: Record
        private val _projectId: MutableStateFlow<Long?> = MutableStateFlow(null)
        private val _startTime = MutableStateFlow(0L)
        private val _endTime = MutableStateFlow(0L)

        val projects = projectsRepository.projects
        val projectId: StateFlow<Long?>
            get() = _projectId.asStateFlow()
        val startTime: StateFlow<Long>
            get() = _startTime.asStateFlow()
        val endTime: StateFlow<Long>
            get() = _endTime.asStateFlow()

        init {
            viewModelScope.launch {
                record = recordsRepo.get(savedStateHandle.get<Long>("id")!!)
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

        suspend fun updateRecord(): Int {
            if (isModified) {
                return if (_projectId.value == null) {
                    -1 // 项目名为空
                } else {
                    if (_projectId.value in projects) {
                        record.project = _projectId.value!!
                        record.startTime = startTime.value
                        record.endTime = endTime.value
                        record.date = TimeUtils.getIntDate(record.startTime)
                        recordsRepo.update(record)
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
            viewModelScope.launch {
                recordsRepo.delete(record)
            }
        }

        fun setProject(projectId: Long) {
            _projectId.value = projectId
        }

        fun setStartTime(startTime: Long) {
            _startTime.value = startTime
        }

        fun setEndTime(endTime: Long) {
            _endTime.value = endTime
        }
    }
