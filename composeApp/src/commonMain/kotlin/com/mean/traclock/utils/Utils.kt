package com.mean.traclock.utils

import kotlin.math.round

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect object Utils {
    fun openUrl(url: String)
}

fun Float.toPercentage(): String {
    val percentValue = this * 100
    val roundedValue = round(percentValue * 100) / 100.0
    return "$roundedValue%"
}
