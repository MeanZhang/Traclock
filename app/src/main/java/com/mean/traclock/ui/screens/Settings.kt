package com.mean.traclock.ui.screens

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Feedback
import androidx.compose.material.icons.outlined.Help
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.SettingsBackupRestore
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.mean.traclock.R
import com.mean.traclock.ui.AboutActivity
import com.mean.traclock.ui.BackupRestoreActivity
import com.mean.traclock.ui.components.SettingGroupTitle
import com.mean.traclock.ui.components.SettingItem

@Composable
fun Settings(context: Context, contentPadding: PaddingValues) {
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
                val intent = Intent(context, BackupRestoreActivity::class.java)
                context.startActivity(intent)
            }
        )
        Divider(color = MaterialTheme.colorScheme.inverseOnSurface)
        SettingGroupTitle(stringResource(R.string.others))
        SettingItem(
            Icons.Outlined.Info,
            stringResource(R.string.title_activity_about),
            stringResource(R.string.settings_description_about),
            onClick = {
                context.startActivity(Intent(context,AboutActivity::class.java))
            }
        )
        SettingItem(
            Icons.Outlined.Help,
            stringResource(R.string.help),
            stringResource(R.string.settings_description_help)
        )
        SettingItem(
            Icons.Outlined.Feedback,
            stringResource(R.string.feedback),
            stringResource(R.string.settings_description_feedback)
        )
    }
}