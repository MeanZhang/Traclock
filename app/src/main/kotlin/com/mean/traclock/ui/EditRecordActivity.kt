package com.mean.traclock.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.loper7.date_time_picker.dialog.CardDatePickerDialog
import com.mean.traclock.App
import com.mean.traclock.R
import com.mean.traclock.database.Record
import com.mean.traclock.ui.components.TopBar
import com.mean.traclock.ui.theme.TraclockTheme
import com.mean.traclock.ui.utils.SetSystemBar
import com.mean.traclock.utils.Database
import com.mean.traclock.utils.getDateTimeString
import com.mean.traclock.viewmodels.EditRecordViewModel
import com.mean.traclock.viewmodels.EditRecordViewModelFactory
import kotlinx.coroutines.flow.MutableStateFlow

class EditRecordActivity : AppCompatActivity() {
    private var showDialog = MutableStateFlow(false)
    private var isModified = false

    @SuppressLint("UnrememberedMutableState")
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val viewModel by viewModels<EditRecordViewModel> {
            EditRecordViewModelFactory(
                getRecord(intent)
            )
        }
        isModified = viewModel.isModified()

        setContent {
            TraclockTheme {
                SetSystemBar()
                val project by viewModel.project.collectAsState("")
                val startTime by viewModel.startTime.collectAsState(0L)
                val endTime by viewModel.endTime.collectAsState(0L)
                val showMenu = mutableStateOf(false)
                val showDialogState by showDialog.collectAsState(false)
                var showProjectsDialog by remember { mutableStateOf(false) }

                val builder = CardDatePickerDialog.builder(this).showBackNow(false)
                    .setThemeColor(MaterialTheme.colorScheme.primary.toArgb())

                val scrollBehavior = remember { TopAppBarDefaults.pinnedScrollBehavior() }

                Scaffold(
                    topBar = {
                        TopBar(
                            navigationIcon = {
                                IconButton(onClick = { finish() }) {
                                    Icon(Icons.Filled.ArrowBack, getString(R.string.back))
                                }
                            },
                            title = getString(R.string.edit_record),
                            actions = {
                                IconButton(onClick = { showMenu.value = true }) {
                                    Icon(Icons.Filled.MoreHoriz, stringResource(R.string.more))
                                }
                                DropdownMenu(
                                    expanded = showMenu.value,
                                    onDismissRequest = { showMenu.value = false }
                                ) {
                                    DropdownMenuItem(
                                        text = { Text(stringResource(R.string.delete)) },
                                        onClick = {
                                            Database.deleteRecord(viewModel.record)
                                            super.finish()
                                        }
                                    )
                                }
                            },
                            scrollBehavior = scrollBehavior
                        )
                    },
                    modifier = Modifier
                        .nestedScroll(scrollBehavior.nestedScrollConnection),
                ) {
                    Column(
                        modifier = Modifier
                            .padding(it)
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                stringResource(R.string.project),
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.weight(1f)
                            )
                            OutlinedButton(
                                onClick = { showProjectsDialog = true },
                                modifier = Modifier.weight(3f)
                            ) {
                                Text(project)
                            }
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                stringResource(R.string.start),
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.weight(1f)
                            )
                            OutlinedButton(
                                onClick = {
                                    builder.setDefaultTime(startTime)
                                    builder.setOnChoose {
                                        viewModel.setStartTime(it)
                                        isModified = viewModel.isModified()
                                    }
                                    builder.build().show()
                                },
                                modifier = Modifier.weight(3f)
                            ) {
                                Text(getDateTimeString(startTime))
                            }
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                stringResource(R.string.end),
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.weight(1f)
                            )
                            OutlinedButton(
                                onClick = {
                                    builder.setDefaultTime(endTime)
                                    builder.setOnChoose {
                                        viewModel.setEndTime(it)
                                        isModified = viewModel.isModified()
                                    }
                                    builder.build().show()
                                },
                                modifier = Modifier.weight(3f)
                            ) {
                                Text(getDateTimeString(endTime))
                            }
                        }
                        Button(onClick = {
                            when (viewModel.updateRecord()) {
                                2 -> super.finish()
                                1 -> super.finish()
                                -1 -> Toast.makeText(
                                    this@EditRecordActivity,
                                    this@EditRecordActivity.getString(R.string.please_enter_a_project_name),
                                    Toast.LENGTH_SHORT
                                ).show()
                                -2 -> Toast.makeText(
                                    this@EditRecordActivity,
                                    this@EditRecordActivity.getString(R.string.no_such_project),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }) {
                            Text(stringResource(R.string.save))
                        }
                    }
                }
                if (showProjectsDialog) {
                    AlertDialog(
                        onDismissRequest = { showProjectsDialog = false },
                        title = { Text(stringResource(R.string.projects)) },
                        confirmButton = {},
                        text = {
                            LazyColumn {
                                items(App.projects.toList()) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.clickable {
                                            viewModel.setProject(it.first)
                                            isModified = viewModel.isModified()
                                            showProjectsDialog = false
                                        }
                                    ) {
                                        RadioButton(
                                            selected = project == it.first,
                                            colors = RadioButtonDefaults.colors(
                                                selectedColor = Color(it.second),
                                                unselectedColor = Color(it.second)
                                            ),
                                            onClick = {
                                                viewModel.setProject(it.first)
                                                isModified = viewModel.isModified()
                                                showProjectsDialog = false
                                            }
                                        )
                                        Text(it.first, Modifier.fillMaxWidth())
                                    }
                                }
                            }
                        }
                    )
                }
                if (showDialogState) {
                    AlertDialog(
                        onDismissRequest = { showDialog.value = false },
                        title = { Text(stringResource(R.string.discard_changes)) },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    showDialog.value = false
                                }
                            ) {
                                Text(
                                    stringResource(R.string.keep_editing),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        },
                        dismissButton = {
                            TextButton(
                                onClick = {
                                    super.finish()
                                }
                            ) {
                                Text(
                                    stringResource(R.string.discard),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        },
                        text = { Text(stringResource(R.string.discard_text)) }
                    )
                }
            }
        }
    }

    override fun finish() {
        if (isModified) {
            showDialog.value = true
        } else {
            super.finish()
        }
    }

    private fun getRecord(intent: Intent): Record {
        val record = Record(
            intent.getStringExtra("project") ?: "",
            intent.getLongExtra("startTime", 0L),
            intent.getLongExtra("endTime", 0L),
            intent.getIntExtra("date", 0)
        )
        record.id = intent.getIntExtra("id", 0)
        return record
    }
}
