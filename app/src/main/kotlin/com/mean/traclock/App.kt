package com.mean.traclock

import android.annotation.SuppressLint
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.IntentFilter
import android.os.Build
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.elvishew.xlog.LogConfiguration
import com.elvishew.xlog.LogLevel
import com.elvishew.xlog.XLog
import com.jakewharton.threetenabp.AndroidThreeTen
import com.mean.traclock.database.AppDatabase
import com.mean.traclock.utils.Config
import com.mean.traclock.utils.NotificationBroadcastReceiver
import com.mean.traclock.utils.Timer
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlin.concurrent.thread

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "timing")

class App : Application() {
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
        val projects = mutableMapOf<String, Int>()
        val isTiming = MutableStateFlow(false)
        val projectName = MutableStateFlow("")
        val startTime = MutableStateFlow(0L)
    }

    override fun onCreate() {
        super.onCreate()

        context = applicationContext
        initXLog()
        initTimingControl()
        AndroidThreeTen.init(this)

        thread {
            initProjectsList()
        }
        initBroadcast()
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun initTimingControl() {
        GlobalScope.launch {
            context.dataStore.data.first().let {
                isTiming.value = it[booleanPreferencesKey("isTiming")] ?: false
                projectName.value = it[stringPreferencesKey("projectName")] ?: ""
                startTime.value = it[longPreferencesKey("startTime")] ?: 0L
                XLog.d(
                    "读取数据：isTiming=%s，projectName=%s，startTime=%s",
                    isTiming.value,
                    projectName.value,
                    startTime.value
                )
                initNotification()
            }
        }
    }

    private fun initXLog() {
        val config = LogConfiguration.Builder()
            .logLevel(if (BuildConfig.DEBUG) LogLevel.ALL else LogLevel.NONE)
            // .enableThreadInfo() // 允许打印线程信息
            .enableStackTrace(2) // 允许打印深度为 2 的调用栈信息
            .enableBorder() // 允许打印日志边框
            .build()
        // 默认 TAG 为“X-LOG”
        XLog.init(config)
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

    private fun initNotification() {
        XLog.d("初始化通知")
        createNotificationChannels()
        if (isTiming.value) {
            Timer.startNotify()
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
