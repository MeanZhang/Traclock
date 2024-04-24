package com.mean.traclock.viewmodels

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mean.traclock.data.Project
import com.mean.traclock.data.Record
import com.mean.traclock.data.repository.ProjectsRepository
import com.mean.traclock.data.repository.RecordsRepository
import com.mean.traclock.data.repository.TimerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class ProjectViewModel
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle,
        private val projectsRepo: ProjectsRepository,
        private val recordsRepo: RecordsRepository,
        private val timerRepo: TimerRepository,
    ) : ViewModel() {
        val project: Project =
            runBlocking(Dispatchers.IO) {
                projectsRepo.get(savedStateHandle.get<Long>("id")!!)
            }
        private val _projectId: MutableStateFlow<Long> = MutableStateFlow(project.id)
        private var _records: Flow<Map<Int, List<Record>>> = flowOf(mapOf())
        private var _timeOfDays: Flow<Map<Int, Long>> = flowOf(mapOf())
        val projectId: StateFlow<Long?>
            get() = _projectId
        val records: Flow<Map<Int, List<Record>>>
            get() = _records
        val timeOfDays: Flow<Map<Int, Long>>
            get() = _timeOfDays

        val projectName: String
            get() = project.name
        val projectColor: Color
            get() = Color(project.color)

        init {
            viewModelScope.launch {
                _records = recordsRepo.getRecordsOfDays(project.id)
                _timeOfDays = recordsRepo.getTimeOfDays(project.id)
            }
        }

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

        fun deleteProject() {
            viewModelScope.launch {
                projectsRepo.delete(project.id)
            }
        }
    }
