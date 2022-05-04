package com.mean.traclock.utils

import android.util.Log

private const val TAG = "TCLK_TIMBER"

object TLog {
    fun d(message: Any?) {
        Log.d(TAG, message.toString())
    }

    fun e(message: Any?) {
        Log.d(TAG, message.toString())
    }
}
