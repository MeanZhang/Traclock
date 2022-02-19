package com.mean.traclock.ui

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.core.view.WindowCompat
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.mean.traclock.R
import com.mean.traclock.TraclockApplication
import com.mean.traclock.database.AppDatabase
import com.mean.traclock.database.Project
import com.mean.traclock.database.Record
import com.mean.traclock.ui.components.SettingGroupTitleWithoutIcon
import com.mean.traclock.ui.components.SettingItemWinthoutIcon
import com.mean.traclock.ui.theme.TraclockTheme
import com.mean.traclock.util.getIntDate
import java.io.BufferedReader
import java.io.InputStreamReader
import java.time.format.DateTimeParseException
import kotlin.concurrent.thread

class BackupRestoreActivity : ComponentActivity() {
    @SuppressLint("UnrememberedMutableState")
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        val getContentAndRestore =
            registerForActivityResult(ActivityResultContracts.GetContent()) {
                if (it != null) {
                    restore(it)
                } else {
                    Toast.makeText(
                        this,
                        getText(R.string.failed_open_file),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        setContent {
            TraclockTheme {
                ProvideWindowInsets {
                    val systemUiController = rememberSystemUiController()
//                    systemUiController.setSystemBarsColor(Color.Transparent)
//                    systemUiController.systemBarsDarkContentEnabled = !isSystemInDarkTheme()

                    var showDialog by remember { mutableStateOf(false) }

                    Scaffold(
                        modifier = Modifier
                            .padding(rememberInsetsPaddingValues(LocalWindowInsets.current.systemBars)),
                        topBar = {
                            SmallTopAppBar(
                                navigationIcon = {
                                    IconButton(onClick = { finish() }) {
                                        Icon(Icons.Filled.ArrowBack, getString(R.string.back))
                                    }
                                },
                                title = { Text(getString(R.string.title_activity_backup_restore)) }
                            )
                        }) { contentPadding ->
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
                                onClick = { showDialog = true }
                            )
                        }
                    }
                    if (showDialog) {
                        AlertDialog(
                            onDismissRequest = { showDialog = false },
                            title = { Text(stringResource(R.string.confirm_restore)) },
                            text = { Text(stringResource(R.string.confirm_restore_text)) },
                            confirmButton = {
                                TextButton(onClick = {
                                    getContentAndRestore.launch("text/csv")
                                    showDialog = false
                                }) {
                                    Text(stringResource(R.string.restore).uppercase())
                                }
                            },
                            dismissButton = {
                                TextButton(onClick = { showDialog = false }) {
                                    Text(stringResource(R.string.cancel).uppercase())
                                }
                            }
                        )
                    }
                }
            }
        }
    }

    private fun backup() {
        //TODO
    }

    private fun restore(uri: Uri) {
        val contentResolver = this.contentResolver
        contentResolver.openInputStream(uri)?.use { inputStream ->
            BufferedReader(InputStreamReader(inputStream)).use { reader ->
                reader.readLine()//去掉第一行
                var line = reader.readLine()
                while (line != null) {
                    val res = restore(line)
                    if (res < 0) {
                        Toast.makeText(this, res.toString(), Toast.LENGTH_SHORT).show()
                        break
                    }
                    line = reader.readLine()
                }
            }
        }
    }

    private fun restore(line: String): Int {
        val columns = line.split(",")
        //项目 开始时间（2000-01-01 00:00:00） 结束时间
        if (columns.size < 3) {
            return -1
        }
        val projectName = columns[0]
        if (projectName.isBlank()) {
            return -2
        }
        val startTime = try {
            columns[1].toLong()
        } catch (e: DateTimeParseException) {
            Log.d(getString(R.string.debug_tag), e.toString())
            return -3
        }
        val date = getIntDate(startTime)
        val endTime = try {
            columns[2].toLong()
        } catch (e: DateTimeParseException) {
            return -4
        }
        if (projectName !in TraclockApplication.projectsList) {
            TraclockApplication.addProject(projectName)
            Log.d(getString(R.string.debug_tag), projectName)
            thread { AppDatabase.getDatabase(this).projectDao().insert(Project(projectName)) }
        }
        val record = Record(projectName, startTime, endTime, date)
        thread { AppDatabase.getDatabase(this).recordDao().insert(record) }
        return 0
    }
}