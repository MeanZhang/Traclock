package com.mean.traclock.backup

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
import com.mean.traclock.CommonRes
import com.mean.traclock.backup.model.RestoreState
import com.mean.traclock.ui.components.SettingGroupTitle
import com.mean.traclock.ui.components.SettingItem
import com.mean.traclock.utils.TimeUtils
import dev.icerock.moko.resources.compose.stringResource
import io.github.vinceglb.filekit.dialogs.FileKitMode
import io.github.vinceglb.filekit.dialogs.FileKitType
import io.github.vinceglb.filekit.dialogs.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.dialogs.compose.rememberFileSaverLauncher
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackupRestore(
    modifier: Modifier = Modifier,
    viewModel: BackupRestoreViewModel = koinViewModel(),
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
            type = FileKitType.File("csv"),
            mode = FileKitMode.Single,
            title = stringResource(Res.strings.restore_select_file),
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
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, stringResource(CommonRes.strings.back))
                    }
                },
                title = { Text(stringResource(CommonRes.strings.title_activity_backup_restore)) },
                scrollBehavior = scrollBehavior,
            )
        },
        modifier =
            modifier
                .nestedScroll(scrollBehavior.nestedScrollConnection),
    ) { contentPadding ->
        Column(modifier = Modifier.padding(contentPadding)) {
            SettingGroupTitle(stringResource(Res.strings.backup))
            SettingItem(
                title = stringResource(Res.strings.backup),
                description = stringResource(Res.strings.backup_locally),
                onClick = {
                    backupLauncher.launch(
                        suggestedName = "traclock_backup_" + TimeUtils.getDateTimeStringWithoutSeperator(),
                        extension = "csv",
                    )
                },
            )

            HorizontalDivider()
            SettingGroupTitle(stringResource(Res.strings.restore))
            SettingItem(
                title = stringResource(Res.strings.restore_from_file),
                description = stringResource(Res.strings.settings_description_restore_from_file),
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
            title = { Text(stringResource(Res.strings.confirm_restore)) },
            text = { Text(stringResource(Res.strings.confirm_restore_text)) },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.setShowConfirmDialog(false)
                    viewModel.restore()
                }) {
                    Text(stringResource(Res.strings.restore).uppercase())
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.setShowConfirmDialog(false) }) {
                    Text(stringResource(CommonRes.strings.cancel).uppercase())
                }
            },
        )
    }
    if (showBackupDialog) {
        AlertDialog(
            onDismissRequest = { },
            title = { Text(stringResource(if (backingUp) Res.strings.backing_up else Res.strings.backup_completed)) },
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
                    Text(stringResource(CommonRes.strings.ok))
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
                            RestoreState.SUCCESS -> Res.strings.restore_completed
                            RestoreState.FAILED -> Res.strings.restore_failed
                            RestoreState.RESTORING -> Res.strings.restoring
                            RestoreState.NO_DATA -> Res.strings.restore_no_data
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
                    Text(stringResource(CommonRes.strings.ok))
                }
            },
        )
    }
}
