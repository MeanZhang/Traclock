package com.mean.traclock.viewmodels

import androidx.lifecycle.ViewModel
import com.mean.traclock.App
import com.mean.traclock.database.AppDatabase

class MainViewModel : ViewModel() {
    private val recordDao = AppDatabase.getDatabase(App.context).recordDao()
    val records = recordDao.getRecordsOfDays()
    val projectsTimeOfDays = recordDao.getProjectsTimeOfDays()
    val timeOfDays = recordDao.getTimeOfDays()
    val projectsTime = recordDao.getProjectsTime()
}
