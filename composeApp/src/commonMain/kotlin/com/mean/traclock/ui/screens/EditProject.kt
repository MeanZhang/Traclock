package com.mean.traclock.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mean.traclock.ui.components.ColorPicker
import com.mean.traclock.utils.PlatformUtils
import com.mean.traclock.viewmodels.EditProjectViewModel
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import traclock.composeapp.generated.resources.Res
import traclock.composeapp.generated.resources.back
import traclock.composeapp.generated.resources.discard
import traclock.composeapp.generated.resources.discard_changes
import traclock.composeapp.generated.resources.discard_text
import traclock.composeapp.generated.resources.keep_editing
import traclock.composeapp.generated.resources.save

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProject(
    navBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EditProjectViewModel,
) {
    val scope = rememberCoroutineScope()
    var showDialog by remember {
        mutableStateOf(false)
    }

    fun back() {
        if (viewModel.isModified) {
            showDialog = true
        } else {
            navBack()
        }
    }

    val name by viewModel.name.collectAsState("")
    val colorValue by viewModel.color.collectAsState()
    val color = Color(colorValue)
    val contentColor = if (color.luminance() > 0.4f) Color.Black else Color.White

    val state = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(state)
    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { back() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, stringResource(Res.string.back))
                    }
                },
                title = {},
                colors =
                    TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        navigationIconContentColor = contentColor,
                        actionIconContentColor = contentColor,
                    ),
                actions = {
                    if (name.isNotBlank()) {
                        IconButton(onClick = {
                            scope.launch {
                                if (viewModel.updateProject() != -1) {
                                    navBack()
                                } else {
                                    PlatformUtils.toast("项目已存在")
                                    if (!PlatformUtils.isAndroid) {
                                        snackbarHostState.showSnackbar("项目已存在")
                                    }
                                }
                            }
                        }) {
                            Icon(Icons.Filled.Check, stringResource(Res.string.save))
                        }
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    ) { contentPadding ->
        Column(
            Modifier.padding(
                bottom = contentPadding.calculateBottomPadding(),
                start = contentPadding.calculateStartPadding(LocalLayoutDirection.current),
                end = contentPadding.calculateEndPadding(LocalLayoutDirection.current),
            ),
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(240.dp + contentPadding.calculateTopPadding())
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    color,
                                    Color.Black,
                                ),
                            ),
                        ),
            ) {
                OutlinedTextField(
                    value = name,
                    textStyle = TextStyle(color = contentColor),
                    onValueChange = {
                        viewModel.setName(it)
                    },
                    modifier = Modifier.padding(top = contentPadding.calculateTopPadding() / 2),
                )
            }
            ColorPicker(color = color) {
                viewModel.setColor(it.toArgb())
            }
        }
    }
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(stringResource(Res.string.discard_changes)) },
            confirmButton = {
                TextButton(onClick = {
                    showDialog = false
                }) {
                    Text(
                        stringResource(Res.string.keep_editing),
                        fontWeight = FontWeight.Bold,
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDialog = false
                    navBack()
                }) {
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
