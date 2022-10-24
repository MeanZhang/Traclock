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

@Composable
fun Settings(context: Context?, contentPadding: PaddingValues = PaddingValues()) {
    Column(
        modifier = Modifier
            .padding(contentPadding)
            .verticalScroll(rememberScrollState())
    ) {
        SettingGroupTitle(stringResource(R.string.normal))
        SettingItem(
            Icons.Outlined.SettingsBackupRestore,
            stringResource(R.string.title_activity_backup_restore),
            stringResource(R.string.settings_description_backup_restore),
            onClick = {
                context?.startActivity(Intent(context, BackupRestoreActivity::class.java))
            }
        )
        SettingGroupTitle(stringResource(R.string.others))
        SettingItem(
            Icons.Outlined.Feedback,
            stringResource(R.string.title_activity_feedback),
            stringResource(R.string.settings_description_feedback),
            onClick = {
                context?.startActivity(Intent(context, FeedbackActivity::class.java))
            }
        )
        SettingItem(
            Icons.Outlined.Info,
            stringResource(R.string.title_activity_about),
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
