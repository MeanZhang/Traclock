package com.mean.traclock.utils

enum class Platform(name: String) {
    Desktop("Desktop"),
    Android("Android"),
}

expect fun currentPlatform(): Platform

expect fun platformToast(message: String)

object PlatformUtils {
    val currentPlatform: Platform
        get() = currentPlatform()

    val isAndroid: Boolean
        get() = currentPlatform == Platform.Android

    fun toast(message: String) = platformToast(message)
}
