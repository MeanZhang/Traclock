package com.mean.traclock.ui.settings

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts.CreateDocument
import androidx.activity.result.contract.ActivityResultContracts.OpenDocument
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.core.view.WindowCompat
import com.mean.traclock.BuildConfig
import com.mean.traclock.R
import com.mean.traclock.ui.components.SettingGroupTitleWithoutIcon
import com.mean.traclock.ui.components.SettingItemWinthoutIcon
import com.mean.traclock.ui.theme.TraclockTheme
import com.mean.traclock.viewmodels.BackupRestoreViewModel

class BackupRestoreActivity : ComponentActivity() {
    private val viewModel: BackupRestoreViewModel by viewModels()
    private val backupLauncher =
        registerForActivityResult(CreateDocument("text/csv")) {
            if (it != null) {
                viewModel.backup(it)
            } else {
                Toast.makeText(
                    this,
                    getText(R.string.failed_open_file),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    private val restoreLauncher =
        registerForActivityResult(OpenDocument()) {
            if (it != null) {
                viewModel.setRestoreUri(it)
                viewModel.showConfirmDialog.value = true
            } else {
                Toast.makeText(
                    this,
                    getText(R.string.failed_open_file),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            TraclockTheme {
                val state = rememberTopAppBarState()
                val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(state)
                val showBackupDialog by viewModel.showBackupDialog.collectAsState()
                val showRestoreDialog by viewModel.showRestoreDialog.collectAsState()
                val showConfirmDialog by viewModel.showConfirmDialog.collectAsState()
                val backingUp by viewModel.backingUp.collectAsState()
                val restoring by viewModel.restoring.collectAsState()
                val progress by viewModel.progress.collectAsState()
                Scaffold(
                    topBar = {
                        TopAppBar(
                            navigationIcon = {
                                IconButton(onClick = { finish() }) {
                                    Icon(Icons.Filled.ArrowBack, getString(R.string.back))
                                }
                            },
                            title = { Text(getString(R.string.title_activity_backup_restore)) },
                            scrollBehavior = scrollBehavior
                        )
                    },
                    modifier = Modifier
                        .nestedScroll(scrollBehavior.nestedScrollConnection)
                ) { contentPadding ->
                    Column(modifier = Modifier.padding(contentPadding)) {
                        SettingGroupTitleWithoutIcon(stringResource(R.string.backup))
                        SettingItemWinthoutIcon(
                            title = stringResource(R.string.backup),
                            description = stringResource(R.string.backup_locally),
                            onClick = { backupLauncher.launch(BuildConfig.APPLICATION_ID + "_backup.csv") }
                        )

                        SettingGroupTitleWithoutIcon(stringResource(R.string.restore))
                        SettingItemWinthoutIcon(
                            title = stringResource(R.string.restore_from_file),
                            description = stringResource(R.string.settings_description_restore_from_file),
                            onClick = {
                                restoreLauncher.launch(arrayOf("text/*"))
                            }
                        )
                    }
                }
                if (showConfirmDialog) {
                    AlertDialog(
                        modifier = Modifier.wrapContentSize(),
                        onDismissRequest = { viewModel.showConfirmDialog.value = false },
                        title = { Text(stringResource(R.string.confirm_restore)) },
                        text = { Text(stringResource(R.string.confirm_restore_text)) },
                        confirmButton = {
                            TextButton(onClick = {
                                viewModel.showConfirmDialog.value = false
                                viewModel.restore()
                            }) {
                                Text(stringResource(R.string.restore).uppercase())
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { viewModel.showConfirmDialog.value = false }) {
                                Text(stringResource(R.string.cancel).uppercase())
                            }
                        }
                    )
                }
                if (showBackupDialog) {
                    AlertDialog(
                        onDismissRequest = { },
                        title = { Text(stringResource(if (backingUp) R.string.backing_up else R.string.backup_completed)) },
                        text = {
                            LinearProgressIndicator(progress = progress)
                        },
                        confirmButton = {
                            TextButton(
                                enabled = !backingUp,
                                onClick = { viewModel.showBackupDialog.value = false }
                            ) {
                                Text(stringResource(R.string.ok))
                            }
                        }
                    )
                }
                if (showRestoreDialog) {
                    AlertDialog(
                        onDismissRequest = { },
                        title = { Text(stringResource(if (restoring) R.string.restoring else R.string.restore_completed)) },
                        text = {
                            LinearProgressIndicator(progress = progress)
                        },
                        confirmButton = {
                            TextButton(
                                enabled = !restoring,
                                onClick = { viewModel.showRestoreDialog.value = false }
                            ) {
                                Text(stringResource(R.string.ok))
                            }
                        }
                    )
                }
            }
        }
    }
}
