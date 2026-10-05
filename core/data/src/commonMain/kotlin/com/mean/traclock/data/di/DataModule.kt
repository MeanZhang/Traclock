package com.mean.traclock.data.di

import com.mean.traclock.data.repository.DataStoreRepository
import com.mean.traclock.data.repository.ProjectsRepository
import com.mean.traclock.data.repository.RecordWithProjectRepository
import com.mean.traclock.data.repository.RecordsRepository
import com.mean.traclock.database.di.daosModule
import com.mean.traclock.datastore.di.dataStoreSourceModule
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val dataModule =
    module {
        includes(dataStoreSourceModule, daosModule)
        singleOf(::DataStoreRepository)
        singleOf(::ProjectsRepository)
        singleOf(::RecordsRepository)
        singleOf(::RecordWithProjectRepository)
    }
