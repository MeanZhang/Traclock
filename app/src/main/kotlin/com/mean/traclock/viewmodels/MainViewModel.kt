package com.mean.traclock.viewmodels

import androidx.lifecycle.ViewModel
import com.mean.traclock.App
import com.mean.traclock.database.AppDatabase

class MainViewModel : ViewModel() {
    private val recordDao = AppDatabase.getDatabase(App.context).recordDao()
    val records = recordDao.getAll()
    val projectsTimeByDate = recordDao.getProjectsTimeByDate()
    val timeByDate = recordDao.getTimeByDate()
    val projectsTime = recordDao.getProjectsTime()
}
