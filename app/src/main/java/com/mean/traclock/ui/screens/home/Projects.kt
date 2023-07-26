package com.mean.traclock.ui.screens.home

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mean.traclock.data.DataModel
import com.mean.traclock.ui.components.RecordItem
import com.mean.traclock.ui.components.TimingCard
import com.mean.traclock.viewmodels.MainViewModel

@Composable
fun Projects(
    viewModel: MainViewModel = viewModel(),
    navToProject: (Int) -> Unit,
    navToEditRecord: (Long) -> Unit,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    val isTiming by DataModel.dataModel.isRunning.collectAsState(false)
    val projectsTime by viewModel.projectsTime.collectAsState(listOf())
    val timingProject by DataModel.dataModel.projectId.collectAsState()
    val startTime by DataModel.dataModel.startTime.collectAsState()

    LazyColumn(contentPadding = contentPadding, modifier = Modifier.fillMaxSize()) {
        item {
            TimingCard(
                timingProject,
                startTime,
                isTiming,
            )
        }
        projectsTime.forEach {
            item {
                RecordItem(
                    it,
                    Color(DataModel.dataModel.projects[it.project]?.color ?: 0),
                    false,
                    navToProject = navToProject,
                    navToEditRecord = navToEditRecord,
                )
            }
        }
    }
}
