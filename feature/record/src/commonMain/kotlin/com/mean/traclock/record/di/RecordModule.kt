package com.mean.traclock.record.di

import com.mean.traclock.record.viewmodels.EditRecordViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val recordModule =
    module {
        viewModel { (id: Long) -> EditRecordViewModel(id, get(), get()) }
    }
