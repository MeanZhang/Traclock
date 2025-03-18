package com.mean.traclock.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mean.traclock.data.repository.ProjectsRepository
import com.mean.traclock.data.repository.RecordWithProjectRepository
import com.mean.traclock.data.repository.RecordsRepository
import com.mean.traclock.model.RecordWithProject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

class EditRecordViewModel(
    savedStateHandle: SavedStateHandle,
    private val recordsRepo: RecordsRepository,
    private val recordWithProjectRepo: RecordWithProjectRepository,
    projectsRepo: ProjectsRepository,
) : ViewModel() {
    private lateinit var recordWithProject: RecordWithProject
    private val _projectId: MutableStateFlow<Long?> = MutableStateFlow(null)
    private val _projectName: MutableStateFlow<String> = MutableStateFlow("")
    private val _startTime = MutableStateFlow(Instant.DISTANT_PAST)
    private val _endTime = MutableStateFlow(Instant.DISTANT_PAST)

    val projects = projectsRepo.projects
    val projectId: StateFlow<Long?>
        get() = _projectId.asStateFlow()
    val projectName: StateFlow<String>
        get() = _projectName.asStateFlow()
    val startTime: StateFlow<Instant>
        get() = _startTime.asStateFlow()
    val endTime: StateFlow<Instant>
        get() = _endTime.asStateFlow()

    init {
        runBlocking(Dispatchers.IO) {
            recordWithProject = recordWithProjectRepo.get(savedStateHandle.get<Long>("id")!!)
            _projectId.value = recordWithProject.projectId
            _startTime.value = recordWithProject.startTime
            _endTime.value = recordWithProject.endTime
        }
    }

    val isModified: Boolean
        get() =
            if (::recordWithProject.isInitialized.not()) {
                false
            } else {
                _projectId.value != recordWithProject.projectId || _startTime.value != recordWithProject.startTime ||
                    _endTime.value != recordWithProject.endTime
            }

    suspend fun updateRecord(): Int {
        if (isModified) {
            return if (_projectId.value == null) {
                -1 // 项目名为空
            } else {
                recordWithProject.record.projectId = _projectId.value!!
                recordWithProject.record.startTime = startTime.value
                recordWithProject.record.endTime = endTime.value
                recordWithProject.record.date = startTime.value.toLocalDateTime(TimeZone.currentSystemDefault()).date
                recordsRepo.update(recordWithProject.record)
                1
            }
        } else {
            return 2 // 没有变化
        }
    }

    fun deleteRecord() {
        viewModelScope.launch(Dispatchers.IO) {
            recordsRepo.delete(recordWithProject.record)
        }
    }

    fun setProject(projectId: Long) {
        _projectId.value = projectId
    }

    /**
     * 设置开始日期
     * @param startDate 开始日期（不含时区）
     */
    fun setStartDate(startDate: LocalDate) {
        _startTime.value =
            LocalDateTime(
                date = startDate,
                time = _startTime.value.toLocalDateTime(TimeZone.currentSystemDefault()).time,
            ).toInstant(TimeZone.currentSystemDefault())
    }

    /**
     * 设置开始时间
     * @param startTime 开始时间（不含时区）
     */
    fun setStartTime(startTime: LocalTime) {
        _startTime.value =
            LocalDateTime(
                date =
                    _startTime.value
                        .toLocalDateTime(TimeZone.currentSystemDefault()).date,
                time = startTime,
            ).toInstant(TimeZone.currentSystemDefault())
    }

    /**
     * 设置结束日期
     * @param endDate 结束日期（不含时区）
     */
    fun setEndDate(endDate: LocalDate) {
        _endTime.value =
            LocalDateTime(
                date = endDate,
                time = _endTime.value.toLocalDateTime(TimeZone.currentSystemDefault()).time,
            ).toInstant(TimeZone.currentSystemDefault())
    }

    /**
     * 设置结束时间
     * @param endTime 结束时间（不含时区）
     */
    fun setEndTime(endTime: LocalTime) {
        _endTime.value =
            LocalDateTime(
                date = _endTime.value.toLocalDateTime(TimeZone.currentSystemDefault()).date,
                time = endTime,
            ).toInstant(TimeZone.currentSystemDefault())
    }
}
