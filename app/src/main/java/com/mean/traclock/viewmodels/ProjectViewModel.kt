package com.mean.traclock.viewmodels

import androidx.lifecycle.ViewModel
import com.mean.traclock.TraclockApplication
import com.mean.traclock.database.AppDatabase

class ProjectViewModel(val projectName: String) : ViewModel() {
    private val recordDao = AppDatabase.getDatabase(TraclockApplication.context).recordDao()
    val records = recordDao.getAll(projectName)
    val timeByDate = recordDao.getTimeByDate(projectName)
}