package com.mean.traclock.di

import org.koin.core.logger.Level
import org.koin.core.logger.Level.DEBUG
import org.koin.core.logger.Level.ERROR
import org.koin.core.logger.Level.INFO
import org.koin.core.logger.Level.NONE
import org.koin.core.logger.Level.WARNING
import org.koin.core.logger.Logger
import org.koin.core.logger.MESSAGE

fun getLogger(): Logger =
    object : Logger() {
        override fun display(
            level: Level,
            msg: MESSAGE,
        ) {
            when (level) {
                DEBUG -> co.touchlab.kermit.Logger.d(msg)
                INFO -> co.touchlab.kermit.Logger.i(msg)
                ERROR -> co.touchlab.kermit.Logger.e(msg)
                WARNING -> co.touchlab.kermit.Logger.w(msg)
                NONE -> co.touchlab.kermit.Logger.v(msg)
            }
        }
    }
