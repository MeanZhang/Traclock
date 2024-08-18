package com.mean.traclock.utils

import com.mean.traclock.App
import io.github.vinceglb.filekit.core.PlatformFile
import java.io.InputStream
import java.io.OutputStream

actual fun PlatformFile.openOutputStream(): OutputStream? = App.context.contentResolver.openOutputStream(this.uri)

actual fun PlatformFile.openInputStream(): InputStream? = App.context.contentResolver.openInputStream(this.uri)
