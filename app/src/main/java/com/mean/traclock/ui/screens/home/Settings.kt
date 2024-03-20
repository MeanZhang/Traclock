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
import androidx.compose.material3.HorizontalDivider
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
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
) {
    Column(
        modifier =
            modifier
                .padding(contentPadding)
                .verticalScroll(rememberScrollState()),
    ) {
        SettingGroupTitle(stringResource(R.string.normal))

        SettingItem(
            title = stringResource(R.string.title_activity_backup_restore),
            icon = Icons.Outlined.SettingsBackupRestore,
            description = stringResource(R.string.settings_description_backup_restore),
            onClick = navToBackupRestore,
        )
        HorizontalDivider()
        SettingGroupTitle(stringResource(R.string.others))
        SettingItem(
            title = stringResource(R.string.title_activity_feedback),
            icon = Icons.Outlined.Feedback,
            description = stringResource(R.string.settings_description_feedback),
            onClick = navToFeddback,
        )
        SettingItem(
            title = stringResource(R.string.title_activity_about),
            icon = Icons.Outlined.Info,
            description = stringResource(R.string.settings_description_about),
            onClick = navToAbout,
        )
    }
}

@Composable
@Preview(showSystemUi = true)
private fun PreviewSettings() {
    Settings({}, {}, {})
}
