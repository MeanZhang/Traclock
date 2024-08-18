package com.mean.traclock.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.mean.traclock.data.Record
import com.mean.traclock.data.repository.ProjectsRepository
import com.mean.traclock.data.repository.RecordsRepository
import com.mean.traclock.data.repository.TimerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val recordsRepo: RecordsRepository,
    projectsRepo: ProjectsRepository,
    private val timerRepo: TimerRepository,
) : ViewModel() {
    val records = recordsRepo.getRecordsOfDays()
    val projectsTimeOfDays = recordsRepo.getProjectsTimeOfDays()
    val timeOfDays = recordsRepo.getTimeOfDays()
    val projectsTime = recordsRepo.getProjectsTime()

    val projects = projectsRepo.projects

    private val _detailView = MutableStateFlow(true)
    val detailView: StateFlow<Boolean>
        get() = _detailView

    val isTiming = timerRepo.isTiming

    val timingProjectId: Long?
        get() = timerRepo.projectId

    val startTime: Long?
        get() = timerRepo.startTime

    fun changeDetailView() {
        _detailView.value = !_detailView.value
        Logger.d("切换详细视图为${_detailView.value}")
    }

    fun stopTiming() {
        viewModelScope.launch(Dispatchers.IO) {
            timerRepo.stop()
        }
    }

    fun startTiming(projectId: Long) {
        viewModelScope.launch(Dispatchers.IO) { timerRepo.start(projectId) }
    }

    fun deleteRecord(record: Record) {
        viewModelScope.launch(Dispatchers.IO) { recordsRepo.delete(record) }
    }

    fun getProjectsTimeOfDay(date: Int) = recordsRepo.getProjectsTimeOfDay(date)
}
