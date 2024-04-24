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
        private val recordsRepository: RecordsRepository,
        projectsRepository: ProjectsRepository,
        private val timerRepository: TimerRepository,
    ) : ViewModel() {
        val records = recordsRepository.getRecordsOfDays()
        val projectsTimeOfDays = recordsRepository.getProjectsTimeOfDays()
        val timeOfDays = recordsRepository.getTimeOfDays()
        val projectsTime = recordsRepository.getProjectsTime()

        val projects = projectsRepository.projects

        private val _detailView = MutableStateFlow(true)
        val detailView: StateFlow<Boolean>
            get() = _detailView

        val isTiming = timerRepository.isTiming

        val timingProjectId: Long?
            get() = timerRepository.projectId

        val startTime: Long?
            get() = timerRepository.startTime

        fun changeDetailView() {
            _detailView.value = !_detailView.value
            XLog.d("切换详细视图为${_detailView.value}")
        }

        fun stopTiming() {
            viewModelScope.launch {
                timerRepository.stop()
            }
        }

        fun startTiming(projectId: Long) {
            viewModelScope.launch { timerRepository.start(projectId) }
        }

        fun deleteRecord(record: Record) {
            viewModelScope.launch { recordsRepository.delete(record) }
        }

        fun getProjectsTimeOfDay(date: Int) = recordsRepository.getProjectsTimeOfDay(date)
    }
