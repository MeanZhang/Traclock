package com.mean.traclock.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mean.traclock.data.Record
import com.mean.traclock.data.repository.ProjectsRepository
import com.mean.traclock.data.repository.RecordsRepository
import com.mean.traclock.utils.TimeUtils
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
    projectsRepo: ProjectsRepository,
) : ViewModel() {
    private lateinit var record: Record
    private val _projectId: MutableStateFlow<Long?> = MutableStateFlow(null)
    private val _startTime = MutableStateFlow(0L)
    private val _endTime = MutableStateFlow(0L)

    val projects = projectsRepo.projects
    val projectId: StateFlow<Long?>
        get() = _projectId.asStateFlow()
    val startTime: StateFlow<Long>
        get() = _startTime.asStateFlow()
    val endTime: StateFlow<Long>
        get() = _endTime.asStateFlow()

    init {
        runBlocking(Dispatchers.IO) {
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
        viewModelScope.launch(Dispatchers.IO) {
            recordsRepo.delete(record)
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
                time = Instant.fromEpochMilliseconds(_startTime.value).toLocalDateTime(TimeZone.currentSystemDefault()).time,
            ).toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
    }

    /**
     * 设置开始时间
     * @param startTime 开始时间（不含时区）
     */
    fun setStartTime(startTime: LocalTime) {
        _startTime.value =
            LocalDateTime(
                date =
                    Instant.fromEpochMilliseconds(_startTime.value)
                        .toLocalDateTime(TimeZone.currentSystemDefault()).date,
                time = startTime,
            ).toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
    }

    /**
     * 设置结束日期
     * @param endDate 结束日期（不含时区）
     */
    fun setEndDate(endDate: LocalDate) {
        _endTime.value =
            LocalDateTime(
                date = endDate,
                time = Instant.fromEpochMilliseconds(_endTime.value).toLocalDateTime(TimeZone.currentSystemDefault()).time,
            ).toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
    }

    /**
     * 设置结束时间
     * @param endTime 结束时间（不含时区）
     */
    fun setEndTime(endTime: LocalTime) {
        _endTime.value =
            LocalDateTime(
                date = Instant.fromEpochMilliseconds(_endTime.value).toLocalDateTime(TimeZone.currentSystemDefault()).date,
                time = endTime,
            ).toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
    }
}
