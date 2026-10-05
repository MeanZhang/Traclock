package com.mean.traclock.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.mean.traclock.app.utils.ApplyForNotificationPermission
import com.mean.traclock.backup.navigation.BackupRestoreKey
import com.mean.traclock.home.navigation.homeEntries
import com.mean.traclock.home.viewmodels.MainViewModel
import com.mean.traclock.project.navigation.EditProjectKey
import com.mean.traclock.project.navigation.ProjectKey
import com.mean.traclock.project.navigation.projectEntries
import com.mean.traclock.record.navigation.EditRecordKey
import com.mean.traclock.record.navigation.recordEntries
import com.mean.traclock.settings.navigation.AboutKey
import com.mean.traclock.settings.navigation.FeedbackKey
import com.mean.traclock.settings.navigation.OpenSourceLicensesKey
import com.mean.traclock.settings.navigation.settingsEntries
import com.mean.traclock.statistic.navigation.statisticEntry
import com.mean.traclock.ui.components.HomeBottomBar
import com.mean.traclock.ui.navigation.toHomeRoute
import com.mean.traclock.ui.navigation.toNavKey
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun TraclockApp() {
    ApplyForNotificationPermission()
    val mainViewModel: MainViewModel = koinViewModel()
    // 记录当前选中的 tab，配置变更（如旋转屏幕）后仍能回到该页
    val selectedTab = rememberSaveable { mutableStateOf(HomeRoute.TIMELINE.name) }
    val backStack = remember { NavBackStack<NavKey>(HomeRoute.valueOf(selectedTab.value).toNavKey()) }
    val currentTab = backStack.lastOrNull()?.toHomeRoute()
    Column(modifier = Modifier.fillMaxSize()) {
        NavDisplay(
            backStack = backStack,
            modifier = Modifier.weight(1f),
            entryDecorators =
                listOf(
                    rememberSaveableStateHolderNavEntryDecorator(),
                    rememberViewModelStoreNavEntryDecorator(),
                ),
            entryProvider =
                entryProvider {
                    homeEntries(
                        viewModel = mainViewModel,
                        navToProject = { backStack.add(ProjectKey(it)) },
                        navToEditRecord = { backStack.add(EditRecordKey(it)) },
                        navToNewProject = { backStack.add(EditProjectKey()) },
                    )
                    statisticEntry()
                    projectEntries(
                        navBack = { backStack.removeLastOrNull() },
                        navToProject = { backStack.add(ProjectKey(it)) },
                        navToEditProject = { backStack.add(EditProjectKey(it)) },
                        navToEditRecord = { backStack.add(EditRecordKey(it)) },
                    )
                    recordEntries(navBack = { backStack.removeLastOrNull() })
                    settingsEntries(
                        navBack = { backStack.removeLastOrNull() },
                        navToBackupRestore = { backStack.add(BackupRestoreKey) },
                        navToFeedback = { backStack.add(FeedbackKey) },
                        navToAbout = { backStack.add(AboutKey) },
                        navToOpenSourceLicenses = { backStack.add(OpenSourceLicensesKey) },
                    )
                },
        )

        HomeBottomBar(
            currentRoute = currentTab,
            navTo = { tab ->
                selectedTab.value = tab.name
                backStack.clear()
                backStack.add(tab.toNavKey())
            },
        )
    }
}
