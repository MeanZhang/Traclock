package com.mean.traclock.ui.screens

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mean.traclock.App
import com.mean.traclock.R
import com.mean.traclock.database.Project
import com.mean.traclock.database.Record
import com.mean.traclock.ui.EditProjectActivity
import com.mean.traclock.ui.ProjectActivity
import com.mean.traclock.ui.components.DividerWithPadding
import com.mean.traclock.ui.components.SmallOutlinedButton
import com.mean.traclock.ui.components.TimingCard
import com.mean.traclock.ui.components.TopBar
import com.mean.traclock.utils.HORIZONTAL_MARGIN
import com.mean.traclock.utils.TimingControl
import com.mean.traclock.utils.getDurationString
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalMaterial3Api::class)
@DelicateCoroutinesApi
@Composable
fun Projects(
    context: Context,
    projectTime: Flow<List<Record>>,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    val projects by App.projects.collectAsState(listOf())
    val time by projectTime.collectAsState(listOf())
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
            .nestedScroll(scrollBehavior.nestedScrollConnection),
    ) {
        LazyColumn {
            item {
                TimingCard(
                    TimingControl.getProjectName(),
                    TimingControl.getStartTime(),
                    isTiming
                )
            }
            projects.forEach {
                item {
                    ProjectItem(
                        context,
                        it,
                        getDurationString(time.find { pt -> pt.project == it.name }?.endTime ?: 0)
                    )
                    DividerWithPadding()
                }
            }
        }
    }
}

@DelicateCoroutinesApi
@Composable
fun ProjectItem(context: Context, project: Project, time: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                val intent = Intent(context, ProjectActivity::class.java)
                intent.putExtra("projectName", project.name)
                context.startActivity(intent)
            }
            .padding(vertical = 8.dp, horizontal = HORIZONTAL_MARGIN),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Circle,
                    contentDescription = null,
                    tint = Color(project.color),
                    modifier = Modifier
                        .size(32.dp)
                        .padding(8.dp, 0.dp)
                )
                Text(
                    project.name,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            SmallOutlinedButton(
                text = time,
                onClick = { TimingControl.startRecord(project.name) }
            )
        }
    }
}
