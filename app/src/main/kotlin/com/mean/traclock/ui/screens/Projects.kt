package com.mean.traclock.ui.screens

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mean.traclock.App
import com.mean.traclock.R
import com.mean.traclock.ui.EditProjectActivity
import com.mean.traclock.ui.components.RecordItem
import com.mean.traclock.ui.components.TimingCard
import com.mean.traclock.ui.components.TopBar
import com.mean.traclock.viewmodels.MainViewModel
import kotlinx.coroutines.DelicateCoroutinesApi

@OptIn(ExperimentalMaterial3Api::class)
@DelicateCoroutinesApi
@Composable
fun Projects(
    context: Context,
    viewModel: MainViewModel,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    val projectsTime by viewModel.projectsTime.collectAsState(listOf())
    val isTiming: Boolean by App.isTiming.collectAsState(false)
    val scrollBehavior = remember { TopAppBarDefaults.pinnedScrollBehavior() }

    Scaffold(
        topBar = {
            TopBar(
                title = stringResource(R.string.projects),
                scrollBehavior = scrollBehavior,
                actions = {
                    IconButton(onClick = {
                        context.startActivity(
                            Intent(context, EditProjectActivity::class.java)
                        )
                    }) {
                        Icon(Icons.Default.Add, stringResource(R.string.new_project))
                    }
                }
            )
        },
        modifier = Modifier
            .padding(contentPadding)
            .nestedScroll(scrollBehavior.nestedScrollConnection)
    ) {
        LazyColumn(contentPadding = it) {
            item {
                TimingCard(
                    App.projectName.value,
                    App.startTime.value,
                    isTiming
                )
            }
            projectsTime.forEach {
                item {
                    RecordItem(context, it, Color(App.projects[it.project] ?: 0), false)
                }
            }
        }
    }
}
