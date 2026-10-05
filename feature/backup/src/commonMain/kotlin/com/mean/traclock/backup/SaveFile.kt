package com.mean.traclock.backup

import io.github.vinceglb.filekit.PlatformFile

/**
 * 弹出"另存为"对话框，返回用户选择的文件；用户取消则返回 null。
 */
expect suspend fun openFileSaver(
    suggestedName: String,
    extension: String,
): PlatformFile?
