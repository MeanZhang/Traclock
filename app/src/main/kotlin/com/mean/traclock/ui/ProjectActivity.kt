package com.mean.traclock.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.activity.viewModels
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.core.view.WindowCompat
import com.mean.traclock.R
import com.mean.traclock.database.Project
import com.mean.traclock.ui.components.DateTitle
import com.mean.traclock.ui.components.RecordItem
import com.mean.traclock.ui.components.TopBar
import com.mean.traclock.ui.theme.TraclockTheme
import com.mean.traclock.ui.utils.SetSystemBar
import com.mean.traclock.utils.Database
import com.mean.traclock.utils.getDataString
import com.mean.traclock.viewmodels.ProjectViewModel
import com.mean.traclock.viewmodels.ProjectViewModelFactory

class ProjectActivity : ComponentActivity() {
    private val viewModel by viewModels<ProjectViewModel> {
        ProjectViewModelFactory(
            intent.getStringExtra(
                "projectName"
            ) ?: ""
        )
    }
    private val editProjectLauncher =
        registerForActivityResult(StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                result.data?.getStringExtra("projectName")
                    ?.let {
                        viewModel.setProjectName(it)
                        recreate()
                    }
            }
        }

    @SuppressLint("UnrememberedMutableState")
    @OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            TraclockTheme {
                SetSystemBar()

                val showMenu = mutableStateOf(false)

                var showDialog by remember { mutableStateOf(false) }

                val decayAnimationSpec = rememberSplineBasedDecay<Float>()
                val scrollBehavior = remember(decayAnimationSpec) {
                    TopAppBarDefaults.exitUntilCollapsedScrollBehavior(decayAnimationSpec)
                }

                val records by viewModel.records.collectAsState(mapOf())
                val time by viewModel.timeOfDays.collectAsState(mapOf())
                val projectName by viewModel.projectNameFlow.collectAsState()

                Scaffold(
                    topBar = {
                        TopBar(
                            navigationIcon = {
                                IconButton(onClick = { finish() }) {
                                    Icon(Icons.Filled.ArrowBack, getString(R.string.back))
                                }
                            },
                            title = projectName,
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

                                            val intent = Intent(
                                                this@ProjectActivity,
                                                EditProjectActivity::class.java
                                            ).apply { putExtra("projectName", projectName) }
                                            editProjectLauncher.launch(intent)
                                        }
                                    )
                                    DropdownMenuItem(
                                        text = { Text(stringResource(R.string.delete)) },
                                        onClick = {
                                            showMenu.value = false
                                            showDialog = true
                                        }
                                    )
                                }
                            },
                            scrollBehavior = scrollBehavior
                        )
                    },
                    modifier = Modifier
                        .nestedScroll(scrollBehavior.nestedScrollConnection)
                ) { contentPadding ->
                    LazyColumn(
                        modifier = Modifier.padding(contentPadding),
                        contentPadding = WindowInsets.navigationBars.asPaddingValues()
                    ) {
                        records.forEach { (date, data) ->
                            stickyHeader {
                                DateTitle(
                                    date = getDataString(date),
                                    duration = time[date] ?: 0L
                                )
                            }
                            items(data) {
                                RecordItem(
                                    context = this@ProjectActivity,
                                    record = it,
                                    color = Color.Cyan
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
                                        Database.deleteProject(Project(projectName))
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
