package com.mean.traclock.utils

import kotlin.math.round

expect object Utils {
    fun openUrl(url: String)
}

fun Float.toPercentage(): String {
    val percentValue = this * 100
    val roundedValue = round(percentValue * 100) / 100.0
    return "$roundedValue%"
}
