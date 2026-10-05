package com.mean.traclock.database.di

import com.mean.traclock.database.getDatabaseBuilder
import org.koin.dsl.module

internal actual val databaseBuilderModule =
    module {
        single { getDatabaseBuilder(get()) }
    }
