package com.mean.traclock.ui.screens

import android.content.Context
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mean.traclock.App
import com.mean.traclock.R
import com.mean.traclock.ui.components.DateTitle
import com.mean.traclock.ui.components.RecordItem
import com.mean.traclock.ui.components.TimingCard
import com.mean.traclock.ui.components.TopBar
import com.mean.traclock.utils.getDataString
import com.mean.traclock.viewmodels.MainViewModel

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun TimeLine(
    context: Context,
    viewModel: MainViewModel,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    val isTiming by App.isTiming.collectAsState(false)
    var detailView by remember { mutableStateOf(true) }
    val time by viewModel.timeByDate.collectAsState(listOf())
    val recordsAll by viewModel.records.collectAsState(listOf())
    val recordByProjects by viewModel.projectsTimeByDate.collectAsState(listOf())
    val records = if (detailView) recordsAll else recordByProjects
    val decayAnimationSpec = rememberSplineBasedDecay<Float>()
    val scrollBehavior = remember(decayAnimationSpec) {
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(decayAnimationSpec)
    }
    Scaffold(
        topBar = {
            TopBar(
                title = stringResource(R.string.timeline),
                scrollBehavior = scrollBehavior,
                actions = {
                    IconButton(onClick = {
                        detailView = !detailView
                    }) {
                        Icon(Icons.Default.SwapHoriz, stringResource(R.string.change_view))
                    }
                }
            )
        },
        modifier = Modifier
            .padding(contentPadding)
            .nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { innerPadding ->
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = innerPadding
        ) {
            item {
                TimingCard(
                    App.projectName.value,
                    App.startTime.value,
                    isTiming
                )
            }
            items(records.size) { i ->
                val record = records[i]
                val color = Color(App.projects[record.project] ?: 0)
                if (i == 0 || (i > 0 && record.date != records[i - 1].date)) {
                    DateTitle(
                        date = getDataString(record.date),
                        duration = time.find { it.date == record.date }?.time ?: 0L
                    )
                    MenuDefaults.Divider()
                }
                RecordItem(context, record, color, detailView)
            }
        }
    }
}
