package com.mean.traclock.di

import android.content.Context
import com.elvishew.xlog.LogConfiguration
import com.elvishew.xlog.LogLevel
import com.elvishew.xlog.XLog
import com.mean.traclock.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object XLogModule {
    @Provides
    @Singleton
    fun provideXlogConfig(
        @ApplicationContext appContext: Context,
    ): LogConfiguration =
        LogConfiguration.Builder()
            .logLevel(if (BuildConfig.DEBUG) LogLevel.ALL else LogLevel.NONE)
            // .enableThreadInfo() // 允许打印线程信息
            .enableStackTrace(2) // 允许打印深度为 2 的调用栈信息
            .enableBorder() // 允许打印日志边框
            .build().also {
                // 默认 TAG 为“X-LOG”
                XLog.init(it)
            }
}
