package com.mean.traclock.utils

import io.github.vinceglb.filekit.core.PlatformFile
import java.io.InputStream
import java.io.OutputStream

expect fun PlatformFile.openOutputStream(): OutputStream?

expect fun PlatformFile.openInputStream(): InputStream?
