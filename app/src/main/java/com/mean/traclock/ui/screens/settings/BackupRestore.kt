package com.mean.traclock.ui.screens.settings

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.CreateDocument
import androidx.activity.result.contract.ActivityResultContracts.OpenDocument
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mean.traclock.R
import com.mean.traclock.ui.components.SettingGroupTitle
import com.mean.traclock.ui.components.SettingItem
import com.mean.traclock.utils.TimeUtils
import com.mean.traclock.viewmodels.BackupRestoreViewModel
import com.mean.traclock.viewmodels.BackupRestoreViewModel.RestoreState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackupRestore(
    modifier: Modifier = Modifier,
    viewModel: BackupRestoreViewModel = hiltViewModel(),
    navBack: () -> Unit,
) {
    val context = LocalContext.current
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
        rememberLauncherForActivityResult(CreateDocument("text/csv")) {
            if (it != null) {
                viewModel.backup(it)
            }
        }
    val restoreLauncher =
        rememberLauncherForActivityResult(OpenDocument()) {
            if (it != null) {
                viewModel.setRestoreUri(it)
                viewModel.setShowConfirmDialog(true)
            }
        }
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = navBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, stringResource(R.string.back))
                    }
                },
                title = { Text(stringResource(R.string.title_activity_backup_restore)) },
                scrollBehavior = scrollBehavior,
            )
        },
        modifier =
            modifier
                .nestedScroll(scrollBehavior.nestedScrollConnection),
    ) { contentPadding ->
        Column(modifier = Modifier.padding(contentPadding)) {
            SettingGroupTitle(stringResource(R.string.backup))
            SettingItem(
                title = stringResource(R.string.backup),
                description = stringResource(R.string.backup_locally),
                onClick = {
                    backupLauncher.launch(
                        context.getString(R.string.default_label) + "_backup_" + TimeUtils.getDateTime() + ".csv",
                    )
                },
            )

            HorizontalDivider()
            SettingGroupTitle(stringResource(R.string.restore))
            SettingItem(
                title = stringResource(R.string.restore_from_file),
                description = stringResource(R.string.settings_description_restore_from_file),
                onClick = {
                    restoreLauncher.launch(arrayOf("text/*"))
                },
            )
        }
    }
    if (showConfirmDialog) {
        AlertDialog(
            modifier = Modifier.wrapContentSize(),
            onDismissRequest = { viewModel.setShowConfirmDialog(false) },
            title = { Text(stringResource(R.string.confirm_restore)) },
            text = { Text(stringResource(R.string.confirm_restore_text)) },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.setShowConfirmDialog(false)
                    viewModel.restore()
                }) {
                    Text(stringResource(R.string.restore).uppercase())
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.setShowConfirmDialog(false) }) {
                    Text(stringResource(R.string.cancel).uppercase())
                }
            },
        )
    }
    if (showBackupDialog) {
        AlertDialog(
            onDismissRequest = { },
            title = { Text(stringResource(if (backingUp) R.string.backing_up else R.string.backup_completed)) },
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
                    Text(stringResource(R.string.ok))
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
                            RestoreState.SUCCESS -> R.string.restore_completed
                            RestoreState.FAILED -> R.string.restore_failed
                            RestoreState.RESTORING -> R.string.restoring
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
                    Text(stringResource(R.string.ok))
                }
            },
        )
    }
}
