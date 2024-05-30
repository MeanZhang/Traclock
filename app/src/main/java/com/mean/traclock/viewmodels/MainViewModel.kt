package com.mean.traclock.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elvishew.xlog.XLog
import com.mean.traclock.data.Record
import com.mean.traclock.data.repository.ProjectsRepository
import com.mean.traclock.data.repository.RecordsRepository
import com.mean.traclock.data.repository.TimerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel
    @Inject
    constructor(
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
            XLog.d("切换详细视图为${_detailView.value}")
        }

        fun stopTiming() {
            viewModelScope.launch {
                timerRepo.stop()
            }
        }

        fun startTiming(projectId: Long) {
            viewModelScope.launch { timerRepo.start(projectId) }
        }

        fun deleteRecord(record: Record) {
            viewModelScope.launch { recordsRepo.delete(record) }
        }

        fun getProjectsTimeOfDay(date: Int) = recordsRepo.getProjectsTimeOfDay(date)
    }
