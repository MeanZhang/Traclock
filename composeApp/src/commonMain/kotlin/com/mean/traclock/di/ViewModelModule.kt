package com.mean.traclock.di

import com.mean.traclock.statistic.StatisticViewModel
import com.mean.traclock.viewmodels.EditProjectViewModel
import com.mean.traclock.viewmodels.EditRecordViewModel
import com.mean.traclock.viewmodels.MainViewModel
import com.mean.traclock.viewmodels.ProjectViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule =
    module {
        viewModelOf(::EditRecordViewModel)
        viewModelOf(::EditProjectViewModel)
        viewModelOf(::MainViewModel)
        viewModelOf(::ProjectViewModel)
        viewModelOf(::StatisticViewModel)
    }
