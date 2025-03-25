package com.mean.traclock

import android.app.Application
import com.mean.traclock.di.appModule
import com.mean.traclock.di.getLogger
import com.mean.traclock.timer.TimerRepository
import com.mean.traclock.utils.AndroidUtils
import com.mean.traclock.utils.initLogger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidContext
import org.koin.androix.startup.KoinStartup
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.koinConfiguration
import org.koin.java.KoinJavaComponent.inject

@OptIn(KoinExperimentalAPI::class)
class App : Application(), KoinStartup {
    override fun onCreate() {
        super.onCreate()
        initLogger()
        AndroidUtils.init(this)
        initNotification()
    }

    override fun onKoinStartup() =
        koinConfiguration {
            logger(getLogger())
            androidContext(this@App)
            modules(appModule)
        }

    private fun initNotification() {
        CoroutineScope(Dispatchers.IO).launch {
            val timerRepo: TimerRepository by inject(TimerRepository::class.java)
            timerRepo.init()
            timerRepo.updateNotification()
        }
    }
}
