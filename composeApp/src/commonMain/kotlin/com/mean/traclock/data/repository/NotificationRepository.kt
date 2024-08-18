package com.mean.traclock.data.repository

expect class NotificationRepository {
    fun notify(
        projectName: String,
        isRunning: Boolean,
        startTime: Long,
    )
}
