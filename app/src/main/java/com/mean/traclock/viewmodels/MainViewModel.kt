package com.mean.traclock.viewmodels

import androidx.lifecycle.ViewModel
import com.mean.traclock.App
import com.mean.traclock.database.AppDatabase
import com.mean.traclock.ui.BottomNavType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainViewModel : ViewModel() {
    val recordDao = AppDatabase.getDatabase(App.context).recordDao()
    val records = recordDao.getAll()
    val projectsTimeByDate = recordDao.getProjectsTimeByDate()
    val timeByDate = recordDao.getTimeByDate()
    val projectsTime = recordDao.getProjectsTime()

    private val _homeScreenState: MutableStateFlow<BottomNavType> =
        MutableStateFlow(BottomNavType.TIMELINE)

    val homeScreenState: StateFlow<BottomNavType>
        get() = _homeScreenState

    fun setHomeScreenState(homeScreenState: BottomNavType) {
        _homeScreenState.value = homeScreenState
    }
}