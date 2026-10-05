package com.mean.traclock.statistic.di

import com.mean.traclock.statistic.StatisticViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val statisticModule =
    module {
        viewModelOf(::StatisticViewModel)
    }
