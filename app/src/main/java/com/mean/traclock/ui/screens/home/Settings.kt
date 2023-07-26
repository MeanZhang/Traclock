package com.mean.traclock.ui.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Feedback
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.SettingsBackupRestore
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mean.traclock.R
import com.mean.traclock.ui.components.SettingGroupTitle
import com.mean.traclock.ui.components.SettingItem

@Composable
fun Settings(
    navToBackupRestore: () -> Unit,
    navToFeddback: () -> Unit,
    navToAbout: () -> Unit,
    contentPadding: PaddingValues = PaddingValues(),
) {
    Column(
        modifier = Modifier
            .padding(contentPadding)
            .verticalScroll(rememberScrollState()),
    ) {
        SettingGroupTitle(stringResource(R.string.normal))

        SettingItem(
            stringResource(R.string.title_activity_backup_restore),
            Icons.Outlined.SettingsBackupRestore,
            stringResource(R.string.settings_description_backup_restore),
            onClick = navToBackupRestore,
        )
        Divider()
        SettingGroupTitle(stringResource(R.string.others))
        SettingItem(
            stringResource(R.string.title_activity_feedback),
            Icons.Outlined.Feedback,
            stringResource(R.string.settings_description_feedback),
            onClick = navToFeddback,
        )
        SettingItem(
            stringResource(R.string.title_activity_about),
            Icons.Outlined.Info,
            stringResource(R.string.settings_description_about),
            onClick = navToAbout,
        )
    }
}

@Composable
@Preview(showSystemUi = true)
fun PreviewSettings() {
    Settings({}, {}, {})
}
