package com.mean.traclock.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Assignment
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Timeline
import androidx.compose.ui.graphics.vector.ImageVector
import com.mean.traclock.CommonRes
import dev.icerock.moko.resources.StringResource

enum class HomeRoute(
    val titleId: StringResource,
    val icon: ImageVector,
) {
    TIMELINE(CommonRes.strings.timeline, Icons.Outlined.Timeline),
    PROJECTS(CommonRes.strings.projects, Icons.AutoMirrored.Outlined.Assignment),
    STATISTICS(CommonRes.strings.statistics, Icons.Outlined.Analytics),
    SETTINGS(CommonRes.strings.settings, Icons.Outlined.Settings),
}
