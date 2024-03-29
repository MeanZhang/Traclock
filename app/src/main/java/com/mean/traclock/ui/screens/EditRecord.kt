package com.mean.traclock.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.loper7.date_time_picker.dialog.CardDatePickerDialog
import com.mean.traclock.R
import com.mean.traclock.data.DataModel
import com.mean.traclock.utils.TimeUtils
import com.mean.traclock.viewmodels.EditRecordViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditRecord(
    viewModel: EditRecordViewModel,
    modifier: Modifier = Modifier,
    navBack: () -> Unit,
) {
    val context = LocalContext.current
    val projectId by viewModel.projectId.collectAsState()
    val startTime by viewModel.startTime.collectAsState(0L)
    val endTime by viewModel.endTime.collectAsState(0L)
    var showDialog by remember { mutableStateOf(false) }
    var showMenu by remember { mutableStateOf(false) }
    var showProjectsDialog by remember { mutableStateOf(false) }

    val builder =
        CardDatePickerDialog.builder(context).showBackNow(false)
            .setThemeColor(MaterialTheme.colorScheme.primary.toArgb())

    val state = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(state)

    fun back() {
        if (viewModel.isModified) {
            showDialog = true
        } else {
            navBack()
        }
    }
    BackHandler {
        back()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { back() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, stringResource(R.string.back))
                    }
                },
                title = { Text(stringResource(R.string.edit_record)) },
                actions = {
                    IconButton(onClick = { showMenu = true }) {
                        Icon(Icons.Filled.MoreHoriz, stringResource(R.string.more))
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false },
                    ) {
                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.delete)) },
                            onClick = {
                                viewModel.deleteRecord()
                                navBack()
                            },
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },
        modifier =
            modifier
                .nestedScroll(scrollBehavior.nestedScrollConnection),
    ) { contentPadding ->
        Column(
            modifier =
                Modifier
                    .padding(contentPadding)
                    .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    stringResource(R.string.project),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f),
                )
                OutlinedButton(
                    onClick = { showProjectsDialog = true },
                    modifier = Modifier.weight(3f),
                ) {
                    Text(DataModel.dataModel.projects[projectId]?.name ?: "")
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    stringResource(R.string.start),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f),
                )
                OutlinedButton(
                    onClick = {
                        builder.setDefaultTime(startTime)
                        builder.setOnChoose {
                            viewModel.setStartTime(it)
                        }
                        builder.build().show()
                    },
                    modifier = Modifier.weight(3f),
                ) {
                    Text(TimeUtils.getDateTimeString(startTime))
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    stringResource(R.string.end),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f),
                )
                OutlinedButton(
                    onClick = {
                        builder.setDefaultTime(endTime)
                        builder.setOnChoose {
                            viewModel.setEndTime(it)
                        }
                        builder.build().show()
                    },
                    modifier = Modifier.weight(3f),
                ) {
                    Text(TimeUtils.getDateTimeString(endTime))
                }
            }
            Button(onClick = {
                when (viewModel.updateRecord()) {
                    2 -> navBack()
                    1 -> navBack()
                    -1 ->
                        Toast.makeText(
                            context,
                            context.getString(R.string.please_enter_a_project_name),
                            Toast.LENGTH_SHORT,
                        ).show()

                    -2 ->
                        Toast.makeText(
                            context,
                            context.getString(R.string.no_such_project),
                            Toast.LENGTH_SHORT,
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
                    items(DataModel.dataModel.projects.keys.toList()) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier =
                                Modifier.clickable {
                                    viewModel.setProject(it)
                                    showProjectsDialog = false
                                },
                        ) {
                            RadioButton(
                                selected = projectId == it,
                                colors =
                                    RadioButtonDefaults.colors(
                                        selectedColor = Color(DataModel.dataModel.projects[it]!!.color),
                                        unselectedColor = Color(DataModel.dataModel.projects[it]!!.color),
                                    ),
                                onClick = {
                                    viewModel.setProject(it)
                                    showProjectsDialog = false
                                },
                            )
                            Text(DataModel.dataModel.projects[it]!!.name, Modifier.fillMaxWidth())
                        }
                    }
                }
            },
        )
    }
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(stringResource(R.string.discard_changes)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDialog = false
                    },
                ) {
                    Text(
                        stringResource(R.string.keep_editing),
                        fontWeight = FontWeight.Bold,
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDialog = false
                        navBack()
                    },
                ) {
                    Text(
                        stringResource(R.string.discard),
                        fontWeight = FontWeight.Bold,
                    )
                }
            },
            text = { Text(stringResource(R.string.discard_text)) },
        )
    }
}
