package com.mean.traclock.utils

import co.touchlab.kermit.Logger
import co.touchlab.kermit.Severity
import co.touchlab.kermit.platformLogWriter
import com.mean.traclock.BuildConfig

actual fun initLogger() {
    val severity = if (BuildConfig.DEBUG) Severity.Debug else Severity.Info
    Logger.setLogWriters(platformLogWriter())
    Logger.setMinSeverity(severity)
    Logger.setTag(LOG_TAG)
    Logger.i { "Logger initialized, severity: ${severity.name}." }
}
