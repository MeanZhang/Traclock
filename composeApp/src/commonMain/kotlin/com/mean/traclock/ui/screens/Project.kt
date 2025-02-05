package com.mean.traclock.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.mean.traclock.ui.components.DateTitle
import com.mean.traclock.ui.components.NoData
import com.mean.traclock.ui.components.RecordItem
import com.mean.traclock.utils.TimeUtils
import com.mean.traclock.viewmodels.ProjectViewModel
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import traclock.composeapp.generated.resources.Res
import traclock.composeapp.generated.resources.back
import traclock.composeapp.generated.resources.cancel
import traclock.composeapp.generated.resources.confirm_delete_project
import traclock.composeapp.generated.resources.delete
import traclock.composeapp.generated.resources.edit
import traclock.composeapp.generated.resources.more
import traclock.composeapp.generated.resources.no_record

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun Project(
    modifier: Modifier = Modifier,
    navToProject: (Long) -> Unit,
    navToEditProject: (Long) -> Unit,
    navToEditRecord: (Long) -> Unit,
    navBack: () -> Unit,
    viewModel: ProjectViewModel,
) {
    var showMenu by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    val state = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(state)
    val records by viewModel.records.collectAsState(mapOf())
    val time by viewModel.timeOfDays.collectAsState(mapOf())
    val projectId by viewModel.projectId.collectAsState()
    val scope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = navBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, stringResource(Res.string.back))
                    }
                },
                title = {
                    Text(
                        viewModel.projectName,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                },
                actions = {
                    IconButton(onClick = { showMenu = true }) {
                        Icon(Icons.Filled.MoreHoriz, stringResource(Res.string.more))
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false },
                    ) {
                        DropdownMenuItem(
                            text = { Text(stringResource(Res.string.edit)) },
                            onClick = {
                                showMenu = false
                                navToEditProject(projectId!!)
                            },
                        )
                        DropdownMenuItem(
                            text = { Text(stringResource(Res.string.delete)) },
                            onClick = {
                                showMenu = false
                                showDeleteDialog = true
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
        if (records.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier.padding(top = contentPadding.calculateTopPadding()),
                contentPadding = PaddingValues(bottom = contentPadding.calculateBottomPadding()),
            ) {
                records.forEach { (date, data) ->
                    stickyHeader {
                        DateTitle(
                            date = TimeUtils.getDateString(date),
                            duration = time[date] ?: 0L,
                        )
                    }
                    items(data) {
                        RecordItem(
                            record = it,
                            projectName = viewModel.projectName,
                            color = viewModel.projectColor,
                            navToProject = navToProject,
                            navToEditRecord = navToEditRecord,
                            deleteRecord = viewModel::deleteRecord,
                            startTiming = { viewModel.startTimer() },
                        )
                    }
                }
            }
        } else {
            NoData(stringResource(Res.string.no_record), modifier = Modifier.padding(contentPadding))
        }
        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text(stringResource(Res.string.delete)) },
                confirmButton = {
                    TextButton(
                        onClick = {
                            scope.launch {
                                showDeleteDialog = false
                                viewModel.deleteProject()
                                navBack()
                            }
                        },
                    ) {
                        Text(
                            stringResource(Res.string.delete).uppercase(),
                            fontWeight = FontWeight.Bold,
                        )
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { showDeleteDialog = false },
                    ) {
                        Text(
                            stringResource(Res.string.cancel).uppercase(),
                            fontWeight = FontWeight.Bold,
                        )
                    }
                },
                text = { Text(stringResource(Res.string.confirm_delete_project)) },
            )
        }
    }
}
