package com.mean.traclock.viewmodels

import androidx.lifecycle.ViewModel
import com.mean.traclock.App
import com.mean.traclock.database.AppDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainViewModel : ViewModel() {
    private val recordDao = AppDatabase.getDatabase(App.context).recordDao()
    val records = recordDao.getRecordsOfDays()
    val projectsTimeOfDays = recordDao.getProjectsTimeOfDays()
    val timeOfDays = recordDao.getTimeOfDays()
    val projectsTime = recordDao.getProjectsTime()

    private val _detailView = MutableStateFlow(true)
    val detailView: StateFlow<Boolean>
        get() = _detailView

    fun changeDetailView() {
        _detailView.value = !_detailView.value
    }
}
