package com.mean.traclock.project.di

import com.mean.traclock.project.viewmodels.EditProjectViewModel
import com.mean.traclock.project.viewmodels.ProjectViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val projectModule =
    module {
        viewModel { (id: Long) -> ProjectViewModel(id, get(), get(), get()) }
        viewModel { (id: Long) -> EditProjectViewModel(id, get()) }
    }
