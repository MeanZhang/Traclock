package com.mean.traclock.datastore.di

import com.mean.traclock.datastore.getDataStore
import org.koin.core.module.Module
import org.koin.dsl.module

internal actual val dataStoreModule: Module =
    module {
        single { getDataStore(get()) }
    }
