package com.mean.traclock.ui.screens

import android.content.Context
import android.content.Intent
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mean.traclock.R
import com.mean.traclock.ui.components.SettingGroupTitle
import com.mean.traclock.ui.components.SettingItem
import com.mean.traclock.ui.settings.AboutActivity
import com.mean.traclock.ui.settings.BackupRestoreActivity
import com.mean.traclock.ui.settings.FeedbackActivity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Settings(context: Context?, contentPadding: PaddingValues = PaddingValues()) {
    Column(
        modifier = Modifier
            .padding(contentPadding)
            .verticalScroll(rememberScrollState())
    ) {
        SettingGroupTitle(stringResource(R.string.normal))

        SettingItem(
            stringResource(R.string.title_activity_backup_restore),
            Icons.Outlined.SettingsBackupRestore,
            stringResource(R.string.settings_description_backup_restore),
            onClick = {
                context?.startActivity(Intent(context, BackupRestoreActivity::class.java))
            }
        )
        Divider()
        SettingGroupTitle(stringResource(R.string.others))
        SettingItem(
            stringResource(R.string.title_activity_feedback),
            Icons.Outlined.Feedback,
            stringResource(R.string.settings_description_feedback),
            onClick = {
                context?.startActivity(Intent(context, FeedbackActivity::class.java))
            }
        )
        SettingItem(
            stringResource(R.string.title_activity_about),
            Icons.Outlined.Info,
            stringResource(R.string.settings_description_about),
            onClick = {
                context?.startActivity(Intent(context, AboutActivity::class.java))
            }
        )
    }
}

@Composable
@Preview(showSystemUi = true)
fun PreviewSettings() {
    Settings(context = null)
}
