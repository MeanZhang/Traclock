package com.mean.traclock.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.mean.traclock.data.repository.ProjectsRepository
import com.mean.traclock.data.repository.RecordsRepository
import com.mean.traclock.data.repository.TimerRepository
import com.mean.traclock.model.Record
import com.mean.traclock.utils.PlatformUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.Instant
import org.jetbrains.compose.resources.getString
import traclock.composeapp.generated.resources.Res
import traclock.composeapp.generated.resources.is_running_description

class MainViewModel(
    private val recordsRepo: RecordsRepository,
    projectsRepo: ProjectsRepository,
    private val timerRepo: TimerRepository,
) : ViewModel() {
    val daysRecords = recordsRepo.getDaysRecords()
    val daysProjectsDuration = recordsRepo.watchDaysProjectsDuration()
    val timeOfDays = recordsRepo.watchDaysDuration()
    val projectsTime = recordsRepo.watchProjectsDuration()

    val projects = projectsRepo.projects

    private val _detailView = MutableStateFlow(true)
    val detailView: StateFlow<Boolean>
        get() = _detailView

    val isTiming = timerRepo.isTiming

    val timingProjectId: Long?
        get() = timerRepo.projectId

    val startTime: Instant?
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
        viewModelScope.launch(Dispatchers.IO) {
            if (timerRepo.isTiming.value) {
                withContext(Dispatchers.Main) {
                    PlatformUtils.toast(getString(Res.string.is_running_description))
                }
            } else {
                timerRepo.start(projectId)
            }
        }
    }

    fun deleteRecord(record: Record) {
        viewModelScope.launch(Dispatchers.IO) { recordsRepo.delete(record) }
    }
}
