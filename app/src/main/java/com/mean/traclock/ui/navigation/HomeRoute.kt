package com.mean.traclock.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Assignment
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material.icons.outlined.Assignment
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Timeline
import androidx.compose.ui.graphics.vector.ImageVector
import com.mean.traclock.R

enum class HomeRoute(
    val route: String,
    @StringRes val titleId: Int,
    val icon: ImageVector,
) {
    TIMELINE("timeline", R.string.timeline, Icons.Outlined.Timeline),
    PROJECTS("projects", R.string.projects, Icons.AutoMirrored.Outlined.Assignment),
    STATISTICS("statistics", R.string.statistics, Icons.Outlined.Analytics),
    SETTINGS("settings", R.string.settings, Icons.Outlined.Settings),
}
