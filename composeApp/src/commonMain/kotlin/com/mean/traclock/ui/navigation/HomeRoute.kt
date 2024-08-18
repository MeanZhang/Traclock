package com.mean.traclock.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Assignment
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Timeline
import androidx.compose.ui.graphics.vector.ImageVector
import org.jetbrains.compose.resources.StringResource
import traclock.composeapp.generated.resources.Res
import traclock.composeapp.generated.resources.projects
import traclock.composeapp.generated.resources.settings
import traclock.composeapp.generated.resources.statistics
import traclock.composeapp.generated.resources.timeline

enum class HomeRoute(
    val route: String,
    val titleId: StringResource,
    val icon: ImageVector,
) {
    TIMELINE("timeline", Res.string.timeline, Icons.Outlined.Timeline),
    PROJECTS("projects", Res.string.projects, Icons.AutoMirrored.Outlined.Assignment),
    STATISTICS("statistics", Res.string.statistics, Icons.Outlined.Analytics),
    SETTINGS("settings", Res.string.settings, Icons.Outlined.Settings),
}
