package com.mean.traclock.utils

import java.awt.Desktop
import java.net.URI

actual object Utils {
    actual fun openUrl(url: String) {
        val desktop = Desktop.getDesktop()
        if (Desktop.isDesktopSupported() && desktop.isSupported(Desktop.Action.BROWSE)) {
            desktop.browse(URI(url))
        }
    }
}
