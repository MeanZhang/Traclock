package com.mean.traclock.utils

const val LOG_TAG = "TRACLOCK"

/** 由各平台入口在初始化前写入（Android 取自 BuildConfig.DEBUG） */
var isDebug = false

expect fun initLogger()
