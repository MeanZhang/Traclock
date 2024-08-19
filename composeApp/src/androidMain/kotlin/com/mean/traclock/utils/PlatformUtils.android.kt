package com.mean.traclock.utils

import android.os.Looper
import android.widget.Toast
import co.touchlab.kermit.Logger
import com.mean.traclock.App

actual fun platformToast(message: String) {
    if (Looper.getMainLooper() != Looper.myLooper()) {
        throw IllegalAccessError("May only call from main thread.")
    }
    Toast.makeText(App.context, message, Toast.LENGTH_SHORT).show()
    Logger.d { "toast: $message" }
}
