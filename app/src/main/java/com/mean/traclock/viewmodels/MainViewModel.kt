package com.mean.traclock.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mean.traclock.TraclockApplication
import com.mean.traclock.database.AppDatabase
import com.mean.traclock.ui.BottomNavType

class MainViewModel : ViewModel() {
    val recordDao = AppDatabase.getDatabase(TraclockApplication.context).recordDao()
    val records = recordDao.getAll()
    val projectsTimeByDate = recordDao.getProjectsTimeByDate()
    val timeByDate = recordDao.getTimeByDate()
    val projectsTime = recordDao.getProjectsTime()

    private val _homeScreenState: MutableLiveData<BottomNavType> =
        MutableLiveData(BottomNavType.TIMELINE)

    val homeScreenState: LiveData<BottomNavType>
        get() = _homeScreenState

    fun setHomeScreenState(homeScreenState: BottomNavType) {
        _homeScreenState.value = homeScreenState
    }
}