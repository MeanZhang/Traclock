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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.navigation.compose.hiltViewModel
import com.mean.traclock.R
import com.mean.traclock.ui.components.DateTitle
import com.mean.traclock.ui.components.NoData
import com.mean.traclock.ui.components.RecordItem
import com.mean.traclock.utils.TimeUtils
import com.mean.traclock.viewmodels.ProjectViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun Project(
    navToProject: (Long) -> Unit,
    navToEditProject: (Long, Boolean) -> Unit,
    navToEditRecord: (Long) -> Unit,
    navBack: () -> Unit,
    viewModel: ProjectViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
) {
    var showMenu by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    val state = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(state)
    val records by viewModel.records.collectAsState(mapOf())
    val time by viewModel.timeOfDays.collectAsState(mapOf())
    val projectId by viewModel.projectId.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = navBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, stringResource(R.string.back))
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
                        Icon(Icons.Filled.MoreHoriz, stringResource(R.string.more))
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false },
                    ) {
                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.edit)) },
                            onClick = {
                                showMenu = false
                                navToEditProject(projectId!!, false)
                            },
                        )
                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.delete)) },
                            onClick = {
                                showMenu = false
                                showDialog = true
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
                            date = TimeUtils.getDataString(date),
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
            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = { Text(stringResource(R.string.delete)) },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                showDialog = false
                                viewModel.deleteProject()
                                navBack()
                            },
                        ) {
                            Text(
                                stringResource(R.string.delete).uppercase(),
                                fontWeight = FontWeight.Bold,
                            )
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = { showDialog = false },
                        ) {
                            Text(
                                stringResource(R.string.cancel).uppercase(),
                                fontWeight = FontWeight.Bold,
                            )
                        }
                    },
                    text = { Text(stringResource(R.string.confirm_delete_project)) },
                )
            }
        } else {
            NoData(stringResource(R.string.no_record), modifier = Modifier.padding(contentPadding))
        }
    }
}
