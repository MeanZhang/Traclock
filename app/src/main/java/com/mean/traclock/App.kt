package com.mean.traclock

import android.app.Application
import com.elvishew.xlog.LogConfiguration
import com.mean.traclock.data.repository.TimerRepository
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {
    @Inject
    lateinit var xlogConifg: LogConfiguration

    @Inject
    lateinit var timerRepository: TimerRepository

    override fun onCreate() {
        super.onCreate()
//        initXLog()
        initNotification()
    }
//
//    private fun initXLog() {
//        val config =
//            LogConfiguration.Builder()
//                .logLevel(if (BuildConfig.DEBUG) LogLevel.ALL else LogLevel.NONE)
//                // .enableThreadInfo() // 允许打印线程信息
//                .enableStackTrace(2) // 允许打印深度为 2 的调用栈信息
//                .enableBorder() // 允许打印日志边框
//                .build()
//        // 默认 TAG 为“X-LOG”
//        XLog.init(config)
//    }

    private fun initNotification() {
        if (timerRepository.isTiming.value) {
            timerRepository.updateNotification()
        }
    }
}
