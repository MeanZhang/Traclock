package com.mean.traclock.project.viewmodels

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mean.traclock.data.repository.ProjectsRepository
import com.mean.traclock.data.repository.RecordsRepository
import com.mean.traclock.model.Project
import com.mean.traclock.model.Record
import com.mean.traclock.timer.TimerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ProjectViewModel(
    id: Long,
    private val projectsRepo: ProjectsRepository,
    private val recordsRepo: RecordsRepository,
    private val timerRepo: TimerRepository,
) : ViewModel() {
    val project: Project =
        runBlocking(Dispatchers.IO) {
            projectsRepo.get(id)
        }
    private val _records: Flow<Map<Int, List<Record>>> = recordsRepo.watchProjectRecords(project.id)
    private val _timeOfDays: Flow<Map<Int, Long>> = recordsRepo.watchDaysDuration(project.id)
    val projectId: StateFlow<Long?>
        field: MutableStateFlow<Long> = MutableStateFlow(project.id)
    val records: Flow<Map<Int, List<Record>>>
        get() = _records
    val timeOfDays: Flow<Map<Int, Long>>
        get() = _timeOfDays

    val projectName: String
        get() = project.name
    val projectColor: Color
        get() = project.color

    fun deleteRecord(record: Record) {
        viewModelScope.launch {
            recordsRepo.delete(record)
        }
    }

    fun startTimer() {
        viewModelScope.launch {
            timerRepo.start(project.id)
        }
    }

    suspend fun deleteProject() {
        projectsRepo.delete(project.id)
    }
}
