package com.mean.traclock.database.di

import com.mean.traclock.database.getDatabase
import org.koin.core.module.Module
import org.koin.dsl.module

internal expect val databaseBuilderModule: Module

internal val databaseModule =
    module {
        includes(databaseBuilderModule)
        single { getDatabase(get()) }
    }
