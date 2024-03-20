package com.mean.traclock.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mean.traclock.R
import com.mean.traclock.ui.components.ColorPicker
import com.mean.traclock.viewmodels.EditProjectViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProject(
    navBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EditProjectViewModel = viewModel(),
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
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
    BackHandler {
        back()
    }

    val name by viewModel.name.collectAsState("")
    val colorValue by viewModel.color.collectAsState()
    val color = Color(colorValue)
    val contentColor = if (color.luminance() > 0.4f) Color.Black else Color.White

    val state = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(state)
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { back() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, stringResource(R.string.back))
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
                                    Toast.makeText(context, "项目已存在", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }) {
                            Icon(Icons.Filled.Check, stringResource(R.string.save))
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
            title = { Text(stringResource(R.string.discard_changes)) },
            confirmButton = {
                TextButton(onClick = {
                    showDialog = false
                }) {
                    Text(
                        stringResource(R.string.keep_editing),
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
                        stringResource(R.string.discard),
                        fontWeight = FontWeight.Bold,
                    )
                }
            },
            text = { Text(stringResource(R.string.discard_text)) },
        )
    }
}
