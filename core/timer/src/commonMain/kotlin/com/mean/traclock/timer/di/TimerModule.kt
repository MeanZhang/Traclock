package com.mean.traclock.timer.di

import com.mean.traclock.timer.TimerRepository
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal expect val notifierModule: Module

val timerModule =
    module {
        includes(notifierModule)
        singleOf(::TimerRepository)
    }
