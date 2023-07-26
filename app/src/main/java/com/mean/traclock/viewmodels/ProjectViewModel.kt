package com.mean.traclock.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mean.traclock.data.DataModel
import com.mean.traclock.database.Project
import com.mean.traclock.database.Record
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProjectViewModel @Inject constructor(savedStateHandle: SavedStateHandle) : ViewModel() {
    private lateinit var project: Project
    private val _projectId: MutableStateFlow<Int?> = MutableStateFlow(null)
    private var _records: Flow<Map<Int, List<Record>>> = flowOf(mapOf())
    private var _timeOfDays: Flow<Map<Int, Long>> = flowOf(mapOf())
    val projectIdFlow: StateFlow<Int?>
        get() = _projectId
    val records: Flow<Map<Int, List<Record>>>
        get() = _records
    val timeOfDays: Flow<Map<Int, Long>>
        get() = _timeOfDays

    init {
        viewModelScope.launch {
            project = DataModel.dataModel.getProject(savedStateHandle.get<Int>("id")!!)
            _projectId.value = project.id
            _records = DataModel.dataModel.getRecordsOfDays(project.id)
            _timeOfDays = DataModel.dataModel.getTimeOfDays(project.id)
        }
    }
}
