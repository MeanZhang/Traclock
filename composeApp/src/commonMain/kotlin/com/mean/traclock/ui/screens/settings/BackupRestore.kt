package com.mean.traclock.ui.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.mean.traclock.ui.components.SettingGroupTitle
import com.mean.traclock.ui.components.SettingItem
import com.mean.traclock.utils.TimeUtils
import com.mean.traclock.viewmodels.BackupRestoreViewModel
import com.mean.traclock.viewmodels.BackupRestoreViewModel.RestoreState
import io.github.vinceglb.filekit.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.compose.rememberFileSaverLauncher
import io.github.vinceglb.filekit.core.PickerMode
import io.github.vinceglb.filekit.core.PickerType
import org.jetbrains.compose.resources.stringResource
import traclock.composeapp.generated.resources.Res
import traclock.composeapp.generated.resources.back
import traclock.composeapp.generated.resources.backing_up
import traclock.composeapp.generated.resources.backup
import traclock.composeapp.generated.resources.backup_completed
import traclock.composeapp.generated.resources.backup_locally
import traclock.composeapp.generated.resources.cancel
import traclock.composeapp.generated.resources.confirm_restore
import traclock.composeapp.generated.resources.confirm_restore_text
import traclock.composeapp.generated.resources.ok
import traclock.composeapp.generated.resources.restore
import traclock.composeapp.generated.resources.restore_completed
import traclock.composeapp.generated.resources.restore_failed
import traclock.composeapp.generated.resources.restore_from_file
import traclock.composeapp.generated.resources.restoring
import traclock.composeapp.generated.resources.settings_description_restore_from_file
import traclock.composeapp.generated.resources.title_activity_backup_restore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackupRestore(
    modifier: Modifier = Modifier,
    viewModel: BackupRestoreViewModel,
    navBack: () -> Unit,
) {
    val state = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(state)
    val showBackupDialog by viewModel.showBackupDialog.collectAsState()
    val showRestoreDialog by viewModel.showRestoreDialog.collectAsState()
    val showConfirmDialog by viewModel.showConfirmDialog.collectAsState()
    val backingUp by viewModel.backingUp.collectAsState()
    val restoreState by viewModel.restoreState.collectAsState()
    val progress by viewModel.progress.collectAsState()
    val message by viewModel.message.collectAsState()

    val backupLauncher =
        rememberFileSaverLauncher {
            it?.let {
                viewModel.backup(it)
            }
        }
    val restoreLauncher =
        rememberFilePickerLauncher(
            type = PickerType.File(listOf("csv")),
            mode = PickerMode.Single,
        ) {
            it?.let {
                viewModel.setRestoreFile(it)
                viewModel.setShowConfirmDialog(true)
            }
        }
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = navBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, stringResource(Res.string.back))
                    }
                },
                title = { Text(stringResource(Res.string.title_activity_backup_restore)) },
                scrollBehavior = scrollBehavior,
            )
        },
        modifier =
            modifier
                .nestedScroll(scrollBehavior.nestedScrollConnection),
    ) { contentPadding ->
        Column(modifier = Modifier.padding(contentPadding)) {
            SettingGroupTitle(stringResource(Res.string.backup))
            SettingItem(
                title = stringResource(Res.string.backup),
                description = stringResource(Res.string.backup_locally),
                onClick = {
                    backupLauncher.launch(
                        baseName = "traclock_backup_" + TimeUtils.getDateTimeStringWithoutSeperator(),
                        extension = ".csv",
                    )
                },
            )

            HorizontalDivider()
            SettingGroupTitle(stringResource(Res.string.restore))
            SettingItem(
                title = stringResource(Res.string.restore_from_file),
                description = stringResource(Res.string.settings_description_restore_from_file),
                onClick = {
                    restoreLauncher.launch()
                },
            )
        }
    }
    if (showConfirmDialog) {
        AlertDialog(
            modifier = Modifier.wrapContentSize(),
            onDismissRequest = { viewModel.setShowConfirmDialog(false) },
            title = { Text(stringResource(Res.string.confirm_restore)) },
            text = { Text(stringResource(Res.string.confirm_restore_text)) },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.setShowConfirmDialog(false)
                    viewModel.restore()
                }) {
                    Text(stringResource(Res.string.restore).uppercase())
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.setShowConfirmDialog(false) }) {
                    Text(stringResource(Res.string.cancel).uppercase())
                }
            },
        )
    }
    if (showBackupDialog) {
        AlertDialog(
            onDismissRequest = { },
            title = { Text(stringResource(if (backingUp) Res.string.backing_up else Res.string.backup_completed)) },
            text = {
                LinearProgressIndicator(
                    progress = { progress },
                )
            },
            confirmButton = {
                TextButton(
                    enabled = !backingUp,
                    onClick = { viewModel.setShowingBackupDialog(false) },
                ) {
                    Text(stringResource(Res.string.ok))
                }
            },
        )
    }
    if (showRestoreDialog) {
        AlertDialog(
            onDismissRequest = { },
            title = {
                Text(
                    stringResource(
                        when (restoreState) {
                            RestoreState.SUCCESS -> Res.string.restore_completed
                            RestoreState.FAILED -> Res.string.restore_failed
                            RestoreState.RESTORING -> Res.string.restoring
                        },
                    ),
                )
            },
            text = {
                if (restoreState != RestoreState.FAILED) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        LinearProgressIndicator(
                            progress = { progress },
                            modifier =
                                Modifier
                                    .weight(1f)
                                    .padding(end = 4.dp),
                        )
                        Text(
                            "${(progress * 100).toInt()}%".padStart(4),
                            maxLines = 1,
                            fontFamily = FontFamily.Monospace,
                        )
                    }
                } else {
                    Text(message)
                }
            },
            confirmButton = {
                TextButton(
                    enabled = restoreState != RestoreState.RESTORING,
                    onClick = { viewModel.setShowRestoreDialog(false) },
                ) {
                    Text(stringResource(Res.string.ok))
                }
            },
        )
    }
}
