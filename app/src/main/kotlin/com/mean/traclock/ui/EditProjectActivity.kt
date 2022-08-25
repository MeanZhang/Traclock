package com.mean.traclock.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarScrollState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.mean.traclock.R
import com.mean.traclock.ui.components.ColorPicker
import com.mean.traclock.ui.components.TopBar
import com.mean.traclock.ui.theme.TraclockTheme
import com.mean.traclock.viewmodels.EditProjectViewModel
import com.mean.traclock.viewmodels.EditProjectViewModelFactory

class EditProjectActivity : ComponentActivity() {
    private val showDialog = mutableStateOf(false)
    private var isModified = false

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val viewModel by viewModels<EditProjectViewModel> {
            EditProjectViewModelFactory(
                intent.getStringExtra("projectName") ?: ""
            )
        }
        isModified = viewModel.isModified()

        setContent {
            TraclockTheme {
                val name by viewModel.name.collectAsState("")
                val color by viewModel.color.collectAsState(Color.Blue)

                val decayAnimationSpec = rememberSplineBasedDecay<Float>()
                val state = rememberTopAppBarScrollState()
                val scrollBehavior = remember(decayAnimationSpec) {
                    TopAppBarDefaults.exitUntilCollapsedScrollBehavior(decayAnimationSpec, state)
                }
                Scaffold(
                    topBar = {
                        TopBar(
                            navigationIcon = {
                                IconButton(onClick = { finish() }) {
                                    Icon(Icons.Filled.ArrowBack, getString(R.string.back))
                                }
                            },
                            title = name,
                            actions = {
                                if (name.isNotBlank()) {
                                    IconButton(onClick = { save(viewModel) }) {
                                        Icon(Icons.Filled.Check, stringResource(R.string.save))
                                    }
                                }
                            },
                            scrollBehavior = scrollBehavior
                        )
                    },
                    modifier = Modifier
                        .nestedScroll(scrollBehavior.nestedScrollConnection)
                ) {
                    Column(Modifier.padding(it)) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(240.dp)
                                .background(
                                    Brush.verticalGradient(
                                        listOf(
                                            color,
                                            Color.Black
                                        )
                                    )
                                )
                        ) {
                            OutlinedTextField(
                                value = name,
                                onValueChange = {
                                    viewModel.setName(it)
                                    isModified = viewModel.isModified()
                                }
                            )
                        }
                        ColorPicker(
                            onColorSelected = {
                                viewModel.setColor(it)
                                isModified = viewModel.isModified()
                            }
                        )
                    }
                }
                if (showDialog.value) {
                    AlertDialog(
                        onDismissRequest = { showDialog.value = false },
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

    override fun finish() {
        if (isModified) {
            showDialog.value = true
        } else {
            super.finish()
        }
    }

    private fun save(viewModel: EditProjectViewModel) {
        when (viewModel.updateProject()) {
            1 -> {
                val intent = Intent().apply {
                    putExtra("projectName", viewModel.name.value)
                }
                setResult(RESULT_OK, intent)
                super.finish()
            }
            -1 -> Toast.makeText(
                this@EditProjectActivity,
                getString((R.string.project_exists)),
                Toast.LENGTH_SHORT
            ).show()
            0 -> super.finish() // 项目信息没变
        }
    }
}
