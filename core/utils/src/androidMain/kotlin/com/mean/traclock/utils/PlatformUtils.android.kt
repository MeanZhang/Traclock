package com.mean.traclock.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Looper
import android.widget.Toast
import dev.icerock.moko.resources.StringResource

@SuppressLint("StaticFieldLeak")
object AndroidUtils {
    private lateinit var context: Context

    fun init(context: Context) {
        this.context = context
    }

    fun toast(message: String) {
        if (Looper.getMainLooper() != Looper.myLooper()) {
            throw IllegalAccessError("May only call from main thread.")
        }
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun openUrl(url: String) {
        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }

    fun getString(stringRes: StringResource): String {
        return stringRes.getString(context)
    }
}

actual fun toast(message: String) = AndroidUtils.toast(message)

actual fun openUrl(url: String) = AndroidUtils.openUrl(url)

actual fun getString(stringRes: StringResource): String = AndroidUtils.getString(stringRes)
