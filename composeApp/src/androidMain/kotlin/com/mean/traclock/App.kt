package com.mean.traclock

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.mean.traclock.data.database.getAppDatabase
import com.mean.traclock.data.repository.DATA_STORE_FILE_NAME
import com.mean.traclock.data.repository.DatastoreRepository
import com.mean.traclock.data.repository.NotificationRepository
import com.mean.traclock.data.repository.ProjectsRepository
import com.mean.traclock.data.repository.RecordWithProjectRepository
import com.mean.traclock.data.repository.RecordsRepository
import com.mean.traclock.data.repository.TimerRepository
import com.mean.traclock.data.repository.getDataStore
import com.mean.traclock.utils.initLogger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class App : Application() {
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
        lateinit var projectsRepo: ProjectsRepository
        lateinit var recordsRepo: RecordsRepository
        lateinit var recordWithProjectRepo: RecordWithProjectRepository
        lateinit var datastoreRepo: DatastoreRepository

        @SuppressLint("StaticFieldLeak")
        lateinit var notificationRepo: NotificationRepository
        lateinit var timerRepo: TimerRepository
    }

    override fun onCreate() {
        super.onCreate()
        initLogger()
        context = this.applicationContext
        initRepos()
        initNotification()
    }

    private fun initRepos() {
        val database = getAppDatabase(this)
        projectsRepo = ProjectsRepository(database.projectDao(), database.recordDao())
        recordsRepo = RecordsRepository(database.recordDao())
        recordWithProjectRepo = RecordWithProjectRepository(database.recordWithProjectDao())
        datastoreRepo = DatastoreRepository(getDataStore { filesDir.resolve(DATA_STORE_FILE_NAME).absolutePath })
        notificationRepo = NotificationRepository(this)
        timerRepo = TimerRepository(notificationRepo, projectsRepo, recordsRepo, datastoreRepo)
    }

    private fun initNotification() {
        CoroutineScope(Dispatchers.IO).launch {
            timerRepo.updateNotification()
        }
    }
}
