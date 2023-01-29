package com.mean.traclock.viewmodels

import androidx.lifecycle.ViewModel
import com.mean.traclock.data.DataModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainViewModel : ViewModel() {
    val records = DataModel.dataModel.getRecordsOfDays()
    val projectsTimeOfDays = DataModel.dataModel.getProjectsTimeOfDays()
    val timeOfDays = DataModel.dataModel.getTimeOfDays()
    val projectsTime = DataModel.dataModel.getProjectsTime()

    private val _detailView = MutableStateFlow(true)
    val detailView: StateFlow<Boolean>
        get() = _detailView

    fun changeDetailView() {
        _detailView.value = !_detailView.value
    }
}
