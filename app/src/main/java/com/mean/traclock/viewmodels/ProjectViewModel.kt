package com.mean.traclock.viewmodels

import androidx.lifecycle.ViewModel
import com.mean.traclock.App
import com.mean.traclock.database.AppDatabase
import com.mean.traclock.database.Record
import com.mean.traclock.database.TimeByDate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ProjectViewModel(projectName: String) : ViewModel() {
    private val _projectNameFlow = MutableStateFlow(projectName)
    private val recordDao = AppDatabase.getDatabase(App.context).recordDao()
    private var _records = recordDao.getAll(projectName)
    private var _timeByDate = recordDao.getTimeByDate(projectName)

    val projectNameFlow: StateFlow<String>
        get() = _projectNameFlow
    val records: Flow<List<Record>>
        get() = _records
    val timeByDate: Flow<List<TimeByDate>>
        get() = _timeByDate
    fun setProjectName(value: String) {
        _projectNameFlow.value = value
        _records = recordDao.getAll(value)
        _timeByDate = recordDao.getTimeByDate(value)
    }
}
