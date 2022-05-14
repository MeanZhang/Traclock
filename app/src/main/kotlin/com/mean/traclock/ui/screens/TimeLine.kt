package com.mean.traclock.ui.screens

import android.content.Context
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun TimeLine(
    context: Context,
    viewModel: MainViewModel,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    val isTiming by App.isTiming.collectAsState(false)
    var detailView by remember { mutableStateOf(true) }
    val time by viewModel.timeOfDays.collectAsState(mapOf())
    val recordsAll by viewModel.records.collectAsState(mapOf())
    val recordByProjects by viewModel.projectsTimeOfDays.collectAsState(mapOf())
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
        LazyColumn(Modifier.padding(innerPadding)) {
            item {
                TimingCard(
                    App.projectName.value,
                    App.startTime.value,
                    isTiming
                )
            }
            records.forEach { (date, data) ->
                stickyHeader {
                    DateTitle(
                        date = getDataString(date),
                        duration = time[date] ?: 0L
                    )
                }
                items(data) {
                    RecordItem(
                        context = context,
                        record = it,
                        color = Color.Cyan
                    )
                }
            }
        }
    }
}
