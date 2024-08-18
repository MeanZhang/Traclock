package com.mean.traclock.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.mean.traclock.ui.components.DateTimePicker
import com.mean.traclock.viewmodels.EditRecordViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import traclock.composeapp.generated.resources.Res
import traclock.composeapp.generated.resources.back
import traclock.composeapp.generated.resources.delete
import traclock.composeapp.generated.resources.discard
import traclock.composeapp.generated.resources.discard_changes
import traclock.composeapp.generated.resources.discard_text
import traclock.composeapp.generated.resources.edit_record
import traclock.composeapp.generated.resources.end
import traclock.composeapp.generated.resources.keep_editing
import traclock.composeapp.generated.resources.more
import traclock.composeapp.generated.resources.project
import traclock.composeapp.generated.resources.save
import traclock.composeapp.generated.resources.start

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditRecord(
    modifier: Modifier = Modifier,
    viewModel: EditRecordViewModel,
    navBack: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val projectId by viewModel.projectId.collectAsState()
    val startTime by viewModel.startTime.collectAsState()
    val endTime by viewModel.endTime.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var showMenu by remember { mutableStateOf(false) }
    var showProjectsSheet by remember { mutableStateOf(false) }

    val state = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(state)
    val sheetState = rememberModalBottomSheetState()

    fun back() {
        if (viewModel.isModified) {
            showDialog = true
        } else {
            navBack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { back() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, stringResource(Res.string.back))
                    }
                },
                title = { Text(stringResource(Res.string.edit_record)) },
                actions = {
                    IconButton(onClick = { showMenu = true }) {
                        Icon(Icons.Filled.MoreHoriz, stringResource(Res.string.more))
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false },
                    ) {
                        DropdownMenuItem(
                            text = { Text(stringResource(Res.string.delete)) },
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
                    stringResource(Res.string.project),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f),
                )
                OutlinedButton(
                    onClick = { showProjectsSheet = true },
                    modifier = Modifier.weight(3f),
                ) {
                    Text(
                        viewModel.projects[projectId]?.name ?: "",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    stringResource(Res.string.start),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f),
                )
                DateTimePicker(
                    modifier = Modifier.weight(3f),
                    initailTimeMillis = startTime,
                    onDateChoose = { viewModel.setStartDate(it) },
                    onTimeChoose = { viewModel.setStartTime(it) },
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    stringResource(Res.string.end),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f),
                )
                DateTimePicker(
                    modifier = Modifier.weight(3f),
                    initailTimeMillis = endTime,
                    onDateChoose = { viewModel.setEndDate(it) },
                    onTimeChoose = { viewModel.setEndTime(it) },
                )
            }
            Button(onClick = {
                scope.launch {
                    when (viewModel.updateRecord()) {
                        2 -> navBack()
                        1 -> navBack()
                        -1 -> {}
//                            Toast.makeText(
//                                context,
//                                context.getString(R.string.please_enter_a_project_name),
//                                Toast.LENGTH_SHORT,
//                            ).show()

                        -2 -> {}
//                            Toast.makeText(
//                                context,
//                                context.getString(R.string.no_such_project),
//                                Toast.LENGTH_SHORT,
//                            ).show()
                    }
                }
            }) {
                Text(stringResource(Res.string.save))
            }
        }
        if (showProjectsSheet) {
            ModalBottomSheet(
                onDismissRequest = { showProjectsSheet = false },
                sheetState = sheetState,
            ) {
                Column {
                    viewModel.projects.keys.toList().forEach {
                        ListItem(
                            headlineContent = {
                                Text(
                                    viewModel.projects[it]!!.name,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                )
                            },
                            leadingContent = {
                                RadioButton(
                                    selected = projectId == it,
                                    colors =
                                        RadioButtonDefaults.colors(
                                            selectedColor = Color(viewModel.projects[it]!!.color),
                                            unselectedColor = Color(viewModel.projects[it]!!.color),
                                        ),
                                    onClick = null,
                                )
                            },
                            modifier =
                                Modifier.clickable {
                                    scope.launch {
                                        viewModel.setProject(it)
                                        delay(100)
                                        sheetState.hide()
                                    }.invokeOnCompletion {
                                        if (!sheetState.isVisible) {
                                            showProjectsSheet = false
                                        }
                                    }
                                },
                        )
                    }
                }
            }
        }
    }
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(stringResource(Res.string.discard_changes)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDialog = false
                    },
                ) {
                    Text(
                        stringResource(Res.string.keep_editing),
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
                        stringResource(Res.string.discard),
                        fontWeight = FontWeight.Bold,
                    )
                }
            },
            text = { Text(stringResource(Res.string.discard_text)) },
        )
    }
}
