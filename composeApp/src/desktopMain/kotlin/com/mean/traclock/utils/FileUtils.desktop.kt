package com.mean.traclock.utils

import io.github.vinceglb.filekit.core.PlatformFile
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream

actual fun PlatformFile.openOutputStream(): OutputStream? = FileOutputStream(this.file)

actual fun PlatformFile.openInputStream(): InputStream? = FileInputStream(this.file)
