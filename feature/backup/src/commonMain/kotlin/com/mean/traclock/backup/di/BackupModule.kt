package com.mean.traclock.backup.di

import com.mean.traclock.backup.BackupRestoreViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val backupModule =
    module {
        viewModelOf(::BackupRestoreViewModel)
    }
