package com.mean.traclock.di

import com.mean.traclock.data.di.dataModule
import com.mean.traclock.home.di.homeModule
import com.mean.traclock.project.di.projectModule
import com.mean.traclock.record.di.recordModule
import com.mean.traclock.settings.di.settingsModule
import com.mean.traclock.statistic.di.statisticModule
import com.mean.traclock.timer.di.timerModule

val appModule =
    listOf(
        dataModule,
        timerModule,
        homeModule,
        projectModule,
        recordModule,
        settingsModule,
        statisticModule,
    )
