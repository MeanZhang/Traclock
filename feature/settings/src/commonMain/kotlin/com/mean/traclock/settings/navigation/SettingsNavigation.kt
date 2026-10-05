package com.mean.traclock.settings.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.mean.traclock.backup.BackupRestore
import com.mean.traclock.backup.navigation.BackupRestoreKey
import com.mean.traclock.settings.ui.About
import com.mean.traclock.settings.ui.Feedback
import com.mean.traclock.settings.ui.OpenSourceLicenses
import com.mean.traclock.settings.ui.Settings

data object SettingsKey : NavKey

data object FeedbackKey : NavKey

data object AboutKey : NavKey

data object OpenSourceLicensesKey : NavKey

fun EntryProviderScope<NavKey>.settingsEntries(
    navBack: () -> Unit,
    navToBackupRestore: () -> Unit,
    navToFeedback: () -> Unit,
    navToAbout: () -> Unit,
    navToOpenSourceLicenses: () -> Unit,
) {
    entry<SettingsKey> {
        Settings(
            navToBackupRestore = navToBackupRestore,
            navToFeddback = navToFeedback,
            navToAbout = navToAbout,
        )
    }
    entry<BackupRestoreKey> {
        BackupRestore(navBack = navBack)
    }
    entry<FeedbackKey> {
        Feedback(navBack = navBack)
    }
    entry<AboutKey> {
        About(
            navBack = navBack,
            navToOpenSourceLicenses = navToOpenSourceLicenses,
        )
    }
    entry<OpenSourceLicensesKey> {
        OpenSourceLicenses(navBack = navBack)
    }
}
