package com.mean.traclock.ui.screens.home

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mean.traclock.data.DataModel
import com.mean.traclock.database.Record
import com.mean.traclock.ui.components.RecordItem
import com.mean.traclock.ui.components.TimingCard
import kotlinx.coroutines.flow.Flow

@Composable
fun Projects(
    projectsTimeFlow: Flow<List<Record>>,
    navToProject: (Int) -> Unit,
    navToEditRecord: (Long) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    val isTiming by DataModel.dataModel.isRunning.collectAsState(false)
    val projectsTime by projectsTimeFlow.collectAsState(listOf())
    val timingProject by DataModel.dataModel.projectId.collectAsState()
    val startTime by DataModel.dataModel.startTime.collectAsState()

    LazyColumn(contentPadding = contentPadding, modifier = modifier) {
        item {
            TimingCard(
                timingProject,
                startTime,
                isTiming,
            )
        }
        items(projectsTime, key = { it.project }) {
            RecordItem(
                record = it,
                color = Color(DataModel.dataModel.projects[it.project]?.color ?: 0),
                detailView = false,
                navToProject = navToProject,
                navToEditRecord = navToEditRecord,
            )
        }
    }
}
