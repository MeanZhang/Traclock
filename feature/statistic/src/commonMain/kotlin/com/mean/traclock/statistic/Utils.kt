package com.mean.traclock.statistic

import kotlin.math.round

fun Float.toPercentage(): String {
    val percentValue = this * 100
    val roundedValue = round(percentValue * 100) / 100.0
    return "$roundedValue%"
}
