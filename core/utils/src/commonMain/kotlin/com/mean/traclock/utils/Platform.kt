package com.mean.traclock.utils

enum class Platform {
    Android,
    Desktop, ;

    companion object {
        val isAndroid: Boolean
            get() = getCurrentPlatform() == Android

        val currentPlatform: Platform
            get() = getCurrentPlatform()
    }
}

internal expect fun getCurrentPlatform(): Platform
