package com.mean.traclock.utils

import co.touchlab.kermit.Logger
import co.touchlab.kermit.Severity
import co.touchlab.kermit.platformLogWriter

actual fun initLogger() {
    val severity = if (isDebug) Severity.Debug else Severity.Info
    Logger.setLogWriters(platformLogWriter())
    Logger.setMinSeverity(severity)
    Logger.setTag(LOG_TAG)
    Logger.i { "Logger initialized, severity: ${severity.name}." }
}
