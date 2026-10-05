package com.mean.traclock.backup

import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.dialogs.openFileSaver

actual suspend fun openFileSaver(
    suggestedName: String,
    extension: String,
): PlatformFile? = FileKit.openFileSaver(suggestedName = suggestedName, defaultExtension = extension)
