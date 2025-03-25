package com.mean.traclock.settings.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.mean.traclock.backup.BackupRestore
import com.mean.traclock.settings.model.SettingsRoute
import com.mean.traclock.ui.HomeRoute

fun NavGraphBuilder.settingsScreen(navController: NavController) {
    composable(HomeRoute.SETTINGS.name) {
        Settings(
            navToBackupRestore = { navController.navigate(SettingsRoute.BACKUP_RESTORE.name) },
            navToFeddback = { navController.navigate(SettingsRoute.FEEDBACK.name) },
            navToAbout = { navController.navigate(SettingsRoute.ABOUT.name) },
            navTo = { navController.navigate(it.name) },
            modifier = Modifier.fillMaxSize(),
        )
    }
    composable(SettingsRoute.BACKUP_RESTORE.name) {
        BackupRestore(
            navBack = { navController.navigateUp() },
        )
    }
    composable(SettingsRoute.FEEDBACK.name) {
        Feedback(navBack = { navController.navigateUp() })
    }
    composable(SettingsRoute.ABOUT.name) {
        About(
            navBack = { navController.navigateUp() },
            navToOpenSourceLicenses = { navController.navigate(SettingsRoute.OPEN_SOURCE_LICENSES.name) },
        )
    }
    composable(SettingsRoute.OPEN_SOURCE_LICENSES.name) {
        OpenSourceLicenses(navBack = { navController.navigateUp() })
    }
}
