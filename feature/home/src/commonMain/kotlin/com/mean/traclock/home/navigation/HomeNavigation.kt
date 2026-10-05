package com.mean.traclock.home.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.mean.traclock.home.ui.Projects
import com.mean.traclock.home.ui.TimeLine
import com.mean.traclock.home.viewmodels.MainViewModel

data object TimelineKey : NavKey

data object ProjectsKey : NavKey

fun EntryProviderScope<NavKey>.homeEntries(
    viewModel: MainViewModel,
    navToProject: (Long) -> Unit,
    navToEditRecord: (Long) -> Unit,
    navToNewProject: () -> Unit,
) {
    entry<TimelineKey> {
        TimeLine(
            viewModel = viewModel,
            navToProject = navToProject,
            navToEditRecord = navToEditRecord,
        )
    }
    entry<ProjectsKey> {
        Projects(
            viewModel = viewModel,
            navToProject = navToProject,
            navToNewProject = navToNewProject,
            modifier = Modifier.fillMaxSize(),
        )
    }
}
