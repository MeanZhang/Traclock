package com.mean.traclock.viewmodels

import androidx.lifecycle.ViewModel
import com.mean.traclock.App
import com.mean.traclock.database.AppDatabase

class ProjectViewModel(val projectName: String) : ViewModel() {
    private val recordDao = AppDatabase.getDatabase(App.context).recordDao()
    val records = recordDao.getAll(projectName)
    val timeByDate = recordDao.getTimeByDate(projectName)
}
