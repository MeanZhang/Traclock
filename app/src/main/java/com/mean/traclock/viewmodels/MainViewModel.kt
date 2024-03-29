package com.mean.traclock.viewmodels

import androidx.lifecycle.ViewModel
import com.elvishew.xlog.XLog
import com.mean.traclock.data.DataModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel
    @Inject
    constructor() : ViewModel() {
        val records = DataModel.dataModel.getRecordsOfDays()
        val projectsTimeOfDays = DataModel.dataModel.getProjectsTimeOfDays()
        val timeOfDays = DataModel.dataModel.getTimeOfDays()
        val projectsTime = DataModel.dataModel.getProjectsTime()

        private val _detailView = MutableStateFlow(true)
        val detailView: StateFlow<Boolean>
            get() = _detailView

        fun changeDetailView() {
            _detailView.value = !_detailView.value
            XLog.d("切换详细视图为${_detailView.value}")
        }
    }
