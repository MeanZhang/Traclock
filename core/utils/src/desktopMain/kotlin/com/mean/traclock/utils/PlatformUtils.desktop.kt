package com.mean.traclock.utils

import dev.icerock.moko.resources.StringResource
import java.awt.Desktop
import java.net.URI

actual fun toast(message: String) {
}

actual fun openUrl(url: String) {
    val desktop = Desktop.getDesktop()
    if (Desktop.isDesktopSupported() && desktop.isSupported(Desktop.Action.BROWSE)) {
        desktop.browse(URI(url))
    }
}

actual fun getString(stringRes: StringResource): String = stringRes.localized()
