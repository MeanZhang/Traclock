package com.mean.traclock.data.repository

import kotlinx.datetime.Instant

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class NotificationRepository {
    fun notify(
        projectName: String,
        isRunning: Boolean,
        startTime: Instant,
    )
}
