package com.mean.traclock.ui.settings

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts.OpenDocument
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.core.view.WindowCompat
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.mean.traclock.App
import com.mean.traclock.R
import com.mean.traclock.ui.components.SettingGroupTitleWithoutIcon
import com.mean.traclock.ui.components.SettingItemWinthoutIcon
import com.mean.traclock.ui.components.TopBar
import com.mean.traclock.ui.theme.TraclockTheme
import com.mean.traclock.ui.utils.SetSystemBar
import com.mean.traclock.utils.RestoreWorker

class BackupRestoreActivity : ComponentActivity() {
    private val showDialog = mutableStateOf(false)
    private var uri: Uri? = null

    private val getContentAndRestore =
        registerForActivityResult(OpenDocument()) {
            if (it != null) {
//                contentResolver.takePersistableUriPermission(
//                    it,
//                    Intent.FLAG_GRANT_READ_URI_PERMISSION
//                )
                uri = it
                showDialog.value = true
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
                SetSystemBar()
                val scrollBehavior = remember { TopAppBarDefaults.pinnedScrollBehavior() }
                Scaffold(
                    topBar = {
                        TopBar(
                            navigationIcon = {
                                IconButton(onClick = { finish() }) {
                                    Icon(Icons.Filled.ArrowBack, getString(R.string.back))
                                }
                            },
                            title = getString(R.string.title_activity_backup_restore),
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
                            onClick = { backup() }
                        )

                        Divider()

                        SettingGroupTitleWithoutIcon(stringResource(R.string.restore))
                        SettingItemWinthoutIcon(
                            title = stringResource(R.string.restore_from_file),
                            description = stringResource(R.string.settings_description_restore_from_file),
                            onClick = {
                                getContentAndRestore.launch(arrayOf("text/*"))
                            }
                        )
                    }
                }
                if (showDialog.value) {
                    AlertDialog(
                        onDismissRequest = { showDialog.value = false },
                        title = { Text(stringResource(R.string.confirm_restore)) },
                        text = { Text(stringResource(R.string.confirm_restore_text)) },
                        confirmButton = {
                            TextButton(onClick = {
                                showDialog.value = false
                                uri?.let { restore(it) }
                            }) {
                                Text(stringResource(R.string.restore).uppercase())
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { showDialog.value = false }) {
                                Text(stringResource(R.string.cancel).uppercase())
                            }
                        }
                    )
                }
            }
        }
    }

    private fun backup() {
        // TODO
    }

    private fun restore(uri: Uri) {
        val data =
            Data.Builder().putString("uri", uri.toString())
                .build()
        val request =
            OneTimeWorkRequest.Builder(RestoreWorker::class.java).setInputData(data)
                .build()
        WorkManager.getInstance(this)
            .enqueueUniqueWork(
                App.context.getString(R.string.restore_work_name),
                ExistingWorkPolicy.KEEP,
                request
            )
    }
}
