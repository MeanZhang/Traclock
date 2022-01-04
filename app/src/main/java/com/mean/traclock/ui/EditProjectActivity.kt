package com.mean.traclock.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.TextField
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
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
import com.google.accompanist.insets.rememberInsetsPaddingValues
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.mean.traclock.R
import com.mean.traclock.viewmodels.EditProjectViewModel
import com.mean.traclock.viewmodels.EditProjectViewModelFactory
import com.mean.traclock.ui.components.ColorPicker
import com.mean.traclock.ui.theme.TraclockTheme

class EditProjectActivity : ComponentActivity() {
    private var showDialog = MutableLiveData(false)
    private var isModified = false

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val viewModel by viewModels<EditProjectViewModel> {
            EditProjectViewModelFactory(
                intent.getStringExtra("name") ?: "",
                intent.getIntExtra("color", Color.Red.toArgb())
            )
        }

        setContent {
            TraclockTheme {
                ProvideWindowInsets {
                    val systemUiController = rememberSystemUiController()
                    systemUiController.setSystemBarsColor(Color.Transparent)
                    systemUiController.systemBarsDarkContentEnabled =
                        androidx.compose.material.MaterialTheme.colors.isLight

                    val name by viewModel.name.observeAsState("")
                    val color by viewModel.color.observeAsState(0)
                    var showDialog by remember { mutableStateOf(false) }

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
                                title = { Text(getString(R.string.edit)) },
                                actions = {
                                    if (name.isNotBlank()) {
                                        IconButton(onClick = { save(viewModel) }) {
                                            Icon(Icons.Filled.Check, stringResource(R.string.save))
                                        }
                                    }
                                })
                        }) { contentPadding ->
                        Column {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .padding(contentPadding)
                                    .fillMaxWidth()
                                    .height(240.dp)
                                    .background(
                                        Brush.verticalGradient(
                                            listOf(
                                                Color(color),
                                                Color.Black
                                            )
                                        )
                                    )
                            ) {
                                TextField(
                                    value = name,
                                    onValueChange = {
                                        viewModel.setName(it)
                                        isModified = viewModel.isModified()
                                    })
                            }
                            ColorPicker(
                                onColorSelected = {
                                    viewModel.setColor(it.toArgb())
                                    isModified = viewModel.isModified()
                                })
                        }
                    }
                    if (showDialog) {
                        AlertDialog(onDismissRequest = { showDialog = false },
                            title = { Text(stringResource(R.string.discard_changes)) },
                            confirmButton = {
                                TextButton(
                                    onClick = {
                                        showDialog = false
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

    private fun save(viewModel: EditProjectViewModel) {
        when (viewModel.updateProject()) {
            1 -> super.finish()
            -1 -> Toast.makeText(
                this@EditProjectActivity,
                getString((R.string.project_exists)),
                Toast.LENGTH_SHORT
            ).show()
            0 -> super.finish()//项目信息没变
        }
    }
}