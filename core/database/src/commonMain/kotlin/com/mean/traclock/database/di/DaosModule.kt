package com.mean.traclock.database.di

import com.mean.traclock.database.TraclockDatabase
import org.koin.dsl.module

val daosModule =
    module {
        includes(databaseModule)
        single { get<TraclockDatabase>().recordDao() }
        single { get<TraclockDatabase>().projectDao() }
        single { get<TraclockDatabase>().recordWithProjectDao() }
    }
