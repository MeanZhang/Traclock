package com.mean.traclock

import android.annotation.SuppressLint
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mean.traclock.database.AppDatabase
import com.mean.traclock.database.Project
import com.mean.traclock.util.TimingControl
import com.mean.traclock.util.startNotify
import kotlinx.coroutines.DelicateCoroutinesApi


class TraclockApplication : Application() {
    companion object {
        fun addProject(projectName: String) {
            TODO("Not yet implemented")
        }

        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
        lateinit var projects: LiveData<List<Project>>
        val projectsList = mutableMapOf<String, Int>()
        val isTiming = MutableLiveData(false)
        val horizontalMargin = 16.dp
    }

    @OptIn(ExperimentalFoundationApi::class, DelicateCoroutinesApi::class)
    override fun onCreate() {
        super.onCreate()

        context = applicationContext

        isTiming.value = TimingControl.getIsTiming()

        projects = AppDatabase.getDatabase(context).projectDao().getAll()
        projects.observeForever {
            projectsList.clear()
            for (project in it) {
                projectsList[project.name] = project.color
            }
        }
        createNotificationChannel()
        initNotification()
    }

    @DelicateCoroutinesApi
    private fun initNotification() {
        if (isTiming.value == true) {
            startNotify(
                TimingControl.getProjectName(),
                TimingControl.getStartTime()
            )
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(
                    getString(R.string.notice_channel_id),
                    getString(R.string.notice_channel_name),
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    description = getString(R.string.notice_channel_description)
                }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}