package com.mean.traclock

import android.annotation.SuppressLint
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.IntentFilter
import android.os.Build
import androidx.compose.foundation.ExperimentalFoundationApi
import com.jakewharton.threetenabp.AndroidThreeTen
import com.mean.traclock.database.AppDatabase
import com.mean.traclock.utils.Config
import com.mean.traclock.utils.NotificationBroadcastReceiver
import com.mean.traclock.utils.TimingControl
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.concurrent.thread

class App : Application() {
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
        val projects = mutableMapOf<String, Int>()
        val isTiming = MutableStateFlow(false)
    }

    @OptIn(ExperimentalFoundationApi::class, DelicateCoroutinesApi::class)
    override fun onCreate() {
        super.onCreate()

        context = applicationContext
        MMKV.initialize(this)
        isTiming.value = TimingControl.getIsTiming()

        thread {
            initProjectsList()
        }
        initNotification()
        initBroadcast()
        AndroidThreeTen.init(this)
    }

    private fun initBroadcast() {
        val broadcastReceiver = NotificationBroadcastReceiver()
        val filter = IntentFilter(Config.NOTIFICATION_ACTION)
        registerReceiver(broadcastReceiver, filter)
    }

    private fun initProjectsList() {
        val projectsList = AppDatabase.getDatabase(context).projectDao().getAll()
        for (project in projectsList) {
            projects[project.name] = project.color
        }
    }

    @DelicateCoroutinesApi
    private fun initNotification() {
        createNotificationChannels()
        if (isTiming.value) {
            TimingControl.startNotify()
        }
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val timingChannel =
                NotificationChannel(
                    Config.TIMING_NOTIFICATION_CHANNEL,
                    getString(R.string.timing_channel_name),
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    description = getString(R.string.timing_channel_description)
                }
            val notificationManager: NotificationManager =
                getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(timingChannel)
        }
    }
}
