package com.mean.traclock.home.di

import com.mean.traclock.home.viewmodels.MainViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val homeModule =
    module {
        viewModelOf(::MainViewModel)
    }
