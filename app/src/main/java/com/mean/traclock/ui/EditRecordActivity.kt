package com.mean.traclock.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.lifecycle.MutableLiveData
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.rememberInsetsPaddingValues
import com.google.accompanist.insets.ui.Scaffold
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.loper7.date_time_picker.dialog.CardDatePickerDialog
import com.mean.traclock.TraclockApplication
import com.mean.traclock.R
import com.mean.traclock.database.Record
import com.mean.traclock.ui.theme.TraclockTheme
import com.mean.traclock.util.Database
import com.mean.traclock.util.getDateTimeString
import com.mean.traclock.viewmodels.EditRecordViewModel
import com.mean.traclock.viewmodels.EditRecordViewModelFactory
import kotlinx.coroutines.launch

class EditRecordActivity : AppCompatActivity() {
    private var showDialog = MutableLiveData(false)
    private var isModified = false

    @SuppressLint("UnrememberedMutableState")
    @OptIn(
        ExperimentalMaterial3Api::class,
        androidx.compose.material.ExperimentalMaterialApi::class
    )
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
                ProvideWindowInsets {
                    val systemUiController = rememberSystemUiController()
                    systemUiController.setSystemBarsColor(Color.Transparent)
                    systemUiController.systemBarsDarkContentEnabled = !isSystemInDarkTheme()

                    val project by viewModel.project.observeAsState("")
                    val startTime by viewModel.startTime.observeAsState(0L)
                    val endTime by viewModel.endTime.observeAsState(0L)

                    val showMenu = mutableStateOf(false)
                    val showDialogState by showDialog.observeAsState(false)

                    val builder = CardDatePickerDialog.builder(this).showBackNow(false)
                        .setThemeColor(MaterialTheme.colorScheme.primary.toArgb())

                    val scrollBehavior = remember { TopAppBarDefaults.pinnedScrollBehavior() }

                    Scaffold(
                        modifier = Modifier
                            .padding(rememberInsetsPaddingValues(LocalWindowInsets.current.systemBars))
                            .nestedScroll(scrollBehavior.nestedScrollConnection),
                        topBar = {
                            SmallTopAppBar(
                                navigationIcon = {
                                    IconButton(onClick = { finish() }) {
                                        Icon(Icons.Filled.ArrowBack, getString(R.string.back))
                                    }
                                },
                                title = { Text(getString(R.string.edit_record)) },
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
                                            })
                                    }
                                },
                                scrollBehavior = scrollBehavior
                            )
                        }) { contentPadding ->
                        val state = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
                        val scope = rememberCoroutineScope()
                        ModalBottomSheetLayout(
                            sheetState = state,
                            sheetContent = {
                                Surface {
                                    val projects by TraclockApplication.projects.observeAsState(
                                        listOf()
                                    )
                                    Column(
                                        modifier = Modifier
                                            .navigationBarsPadding()
                                            .padding(
                                                horizontal = 32.dp,
                                                vertical = 16.dp
                                            )
                                    ) {
                                        Text(
                                            stringResource(R.string.projects),
                                            style = MaterialTheme.typography.headlineSmall,
                                            modifier = Modifier.padding(vertical = 8.dp)
                                        )
                                        for (project1 in projects) {
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .clickable {
                                                        viewModel.setProject(project1.name)
                                                        isModified = viewModel.isModified()
                                                        scope.launch {
                                                            state.hide()
                                                        }
                                                    }
                                                    .padding(vertical = 8.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Circle,
                                                    contentDescription = null,
                                                    tint = Color(project1.color),
                                                    modifier = Modifier
                                                        .size(32.dp)
                                                        .padding(8.dp, 0.dp)
                                                )
                                                Text(
                                                    project1.name,
                                                    style = MaterialTheme.typography.headlineSmall
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        ) {
                            Surface {
                                Column(
                                    modifier = Modifier
                                        .padding(contentPadding)
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
                                            onClick = { scope.launch { state.show() } },
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
                        }
                    }
                    if (showDialogState) {
                        AlertDialog(onDismissRequest = { showDialog.value = false },
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