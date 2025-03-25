package com.mean.traclock.datastore.di

import com.mean.traclock.datastore.DataStoreSource
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal expect val dataStoreModule: Module

val dataStoreSourceModule: Module =
    module {
        includes(dataStoreModule)
        singleOf(::DataStoreSource)
    }
