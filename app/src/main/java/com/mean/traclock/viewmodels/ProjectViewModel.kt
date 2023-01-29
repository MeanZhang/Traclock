package com.mean.traclock.viewmodels

import androidx.lifecycle.ViewModel
import com.mean.traclock.data.DataModel
import com.mean.traclock.database.Record
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ProjectViewModel(projectName: String) : ViewModel() {
    private val _projectNameFlow = MutableStateFlow(projectName)
    private var _records = DataModel.dataModel.getRecordsOfDays(projectName)
    private var _timeOfDays = DataModel.dataModel.getTimeOfDays(projectName)

    val projectNameFlow: StateFlow<String>
        get() = _projectNameFlow
    val records: Flow<Map<Int, List<Record>>>
        get() = _records
    val timeOfDays: Flow<Map<Int, Long>>
        get() = _timeOfDays
    fun setProjectName(value: String) {
        _projectNameFlow.value = value
        _records = DataModel.dataModel.getRecordsOfDays(value)
        _timeOfDays = DataModel.dataModel.getTimeOfDays(value)
    }
}
