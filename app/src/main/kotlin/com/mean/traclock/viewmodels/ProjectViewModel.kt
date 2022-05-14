package com.mean.traclock.viewmodels

import androidx.lifecycle.ViewModel
import com.mean.traclock.App
import com.mean.traclock.database.AppDatabase
import com.mean.traclock.database.Record
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ProjectViewModel(projectName: String) : ViewModel() {
    private val _projectNameFlow = MutableStateFlow(projectName)
    private val recordDao = AppDatabase.getDatabase(App.context).recordDao()
    private var _records = recordDao.getRecordsOfDays(projectName)
    private var _timeOfDays = recordDao.getTimeOfDays(projectName)

    val projectNameFlow: StateFlow<String>
        get() = _projectNameFlow
    val records: Flow<Map<Int, List<Record>>>
        get() = _records
    val timeOfDays: Flow<Map<Int, Long>>
        get() = _timeOfDays
    fun setProjectName(value: String) {
        _projectNameFlow.value = value
        _records = recordDao.getRecordsOfDays(value)
        _timeOfDays = recordDao.getTimeOfDays(value)
    }
}
