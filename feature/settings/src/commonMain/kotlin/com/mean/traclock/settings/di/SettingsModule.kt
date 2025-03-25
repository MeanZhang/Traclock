package com.mean.traclock.settings.di

import com.mean.traclock.backup.di.backupModule
import org.koin.dsl.module

val settingsModule =
    module {
        includes(backupModule)
    }
