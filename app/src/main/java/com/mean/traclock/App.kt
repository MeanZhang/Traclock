package com.mean.traclock

import android.annotation.SuppressLint
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.compose.foundation.ExperimentalFoundationApi
import com.mean.traclock.database.AppDatabase
import com.mean.traclock.database.Project
import com.mean.traclock.utils.TimingControl
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class App : Application() {
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
        lateinit var projects: Flow<List<Project>>
        val projectsList = mutableMapOf<String, Int>()
        val isTiming = MutableStateFlow(false)
    }

    @OptIn(ExperimentalFoundationApi::class, DelicateCoroutinesApi::class)
    override fun onCreate() {
        super.onCreate()

        context = applicationContext

        isTiming.value = TimingControl.getIsTiming()

        projects = AppDatabase.getDatabase(context).projectDao().getAll()
        GlobalScope.launch {
            projects.collect {
                projectsList.clear()
                for (project in it) {
                    projectsList[project.name] = project.color
                }
            }
        }

        createNotificationChannel()
        initNotification()
    }

    @DelicateCoroutinesApi
    private fun initNotification() {
        if (isTiming.value) {
            TimingControl.startNotify()
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
