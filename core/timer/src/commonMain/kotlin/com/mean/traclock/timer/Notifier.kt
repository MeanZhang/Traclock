package com.mean.traclock.timer

import kotlinx.datetime.Instant

interface Notifier {
    fun notify(
        projectName: String,
        isRunning: Boolean,
        startTime: Instant,
    )
}
