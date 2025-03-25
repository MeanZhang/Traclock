package com.mean.traclock.timer.di

import com.mean.traclock.timer.AndroidNotifier
import com.mean.traclock.timer.Notifier
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

internal actual val notifierModule =
    module {
        singleOf(::AndroidNotifier).bind(Notifier::class)
    }
