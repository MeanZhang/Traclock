package com.mean.traclock.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.core.view.WindowCompat
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.mean.traclock.R
import com.mean.traclock.TraclockApplication
import com.mean.traclock.database.Project
import com.mean.traclock.ui.components.*
import com.mean.traclock.ui.theme.TraclockTheme
import com.mean.traclock.util.Database
import com.mean.traclock.util.getDataString
import com.mean.traclock.viewmodels.ProjectViewModel
import com.mean.traclock.viewmodels.ProjectViewModelFactory

class ProjectActivity : ComponentActivity() {
    @SuppressLint("UnrememberedMutableState")
    @OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        val viewModel by viewModels<ProjectViewModel> {
            ProjectViewModelFactory(
                intent.getStringExtra(
                    "projectName"
                ) ?: ""
            )
        }

        setContent {
            TraclockTheme {
                ProvideWindowInsets {
                    val systemUiController = rememberSystemUiController()
//                    systemUiController.setSystemBarsColor(Color.Transparent)
//                    systemUiController.systemBarsDarkContentEnabled = !isSystemInDarkTheme()

                    val showMenu = mutableStateOf(false)

                    var showDialog by remember { mutableStateOf(false) }

                    val scrollBehavior = remember { TopAppBarDefaults.pinnedScrollBehavior() }

//                    val coroutineScope = rememberCoroutineScope()

//                    val projectsListState = rememberLazyListState()
                    val records by viewModel.records.observeAsState(listOf())
                    val time by viewModel.timeByDate.observeAsState(listOf())

                    Scaffold(
                        topBar = {
                            SmallTopAppBar(
                                navigationIcon = {
                                    IconButton(onClick = { finish() }) {
                                        Icon(Icons.Filled.ArrowBack, getString(R.string.back))
                                    }
                                },
                                title = { Text(viewModel.projectName) },
                                actions = {
                                    IconButton(onClick = { showMenu.value = true }) {
                                        Icon(Icons.Filled.MoreHoriz, stringResource(R.string.more))
                                    }
                                    DropdownMenu(
                                        expanded = showMenu.value,
                                        onDismissRequest = { showMenu.value = false }
                                    ) {
                                        DropdownMenuItem(
                                            text = { Text(stringResource(R.string.edit)) },
                                            onClick = {
                                                showMenu.value = false
                                                val intent =
                                                    Intent(
                                                        this@ProjectActivity,
                                                        EditProjectActivity::class.java
                                                    )
                                                intent.putExtra("name", viewModel.projectName)
                                                intent.putExtra(
                                                    "color",
                                                    TraclockApplication.projectsList[viewModel.projectName]
                                                )
                                                startActivity(intent)
                                            })
                                        DropdownMenuItem(
                                            text = { Text(stringResource(R.string.delete)) },
                                            onClick = {
                                                showMenu.value = false
                                                showDialog = true
                                            })
                                    }
                                },
                                scrollBehavior = scrollBehavior
                            )
                        },
                        modifier = Modifier
                            .padding(rememberInsetsPaddingValues(LocalWindowInsets.current.systemBars))
                            .nestedScroll(scrollBehavior.nestedScrollConnection)
                    ) { contentPadding ->
                        LazyColumn(
                            contentPadding = contentPadding
                        ) {
                            items(records.size) { i ->
                                val record = records[i]
                                if (i == 0 || (i > 0 && record.date != records[i - 1].date)) {
                                    DateTitle(
                                        date = getDataString(record.startTime),
                                        duration = time.find { it.date == record.date }?.time
                                            ?: 0L
                                    )
                                    DividerWithPadding()
                                }
                                RecordItemWithoutProject(
                                    context = this@ProjectActivity,
                                    record = records[i]
                                )
                                DividerWithPadding()
                            }
                        }


                        if (showDialog) {
                            AlertDialog(onDismissRequest = { showDialog = false },
                                title = { Text(stringResource(R.string.delete)) },
                                confirmButton = {
                                    TextButton(
                                        onClick = {
                                            Database.deleteProject(Project(viewModel.projectName))
                                            finish()
                                        }
                                    ) {
                                        Text(
                                            stringResource(R.string.delete).uppercase(),
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                },
                                dismissButton = {
                                    TextButton(
                                        onClick = { showDialog = false }
                                    ) {
                                        Text(
                                            stringResource(R.string.cancel).uppercase(),
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                },
                                text = { Text(stringResource(R.string.confirm_delete_project)) }
                            )
                        }
                    }
                }
            }
        }
    }
}