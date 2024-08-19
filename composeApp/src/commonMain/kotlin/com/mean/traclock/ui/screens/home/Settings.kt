package com.mean.traclock.ui.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Feedback
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.SettingsBackupRestore
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import com.mean.traclock.ui.components.SettingGroupTitle
import com.mean.traclock.ui.components.SettingItem
import com.mean.traclock.ui.navigation.HomeRoute
import org.jetbrains.compose.resources.stringResource
import traclock.composeapp.generated.resources.Res
import traclock.composeapp.generated.resources.normal
import traclock.composeapp.generated.resources.others
import traclock.composeapp.generated.resources.settings_description_about
import traclock.composeapp.generated.resources.settings_description_backup_restore
import traclock.composeapp.generated.resources.settings_description_feedback
import traclock.composeapp.generated.resources.title_activity_about
import traclock.composeapp.generated.resources.title_activity_backup_restore
import traclock.composeapp.generated.resources.title_activity_feedback
import ui.components.HomeTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Settings(
    navToBackupRestore: () -> Unit,
    navToFeddback: () -> Unit,
    navToAbout: () -> Unit,
    navTo: (HomeRoute) -> Unit,
    modifier: Modifier = Modifier,
) {
    val state = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(state)
    Scaffold(
        topBar = {
            HomeTopBar(
                currentRoute = HomeRoute.SETTINGS,
                scrollBehavior = scrollBehavior,
            )
        },
        modifier =
            modifier
                .nestedScroll(scrollBehavior.nestedScrollConnection),
    ) { contentPadding ->

        Column(
            modifier =
                modifier
                    .padding(contentPadding)
                    .verticalScroll(rememberScrollState()),
        ) {
            SettingGroupTitle(stringResource(Res.string.normal))

            SettingItem(
                title = stringResource(Res.string.title_activity_backup_restore),
                icon = Icons.Outlined.SettingsBackupRestore,
                description = stringResource(Res.string.settings_description_backup_restore),
                onClick = navToBackupRestore,
            )
            HorizontalDivider()
            SettingGroupTitle(stringResource(Res.string.others))
            SettingItem(
                title = stringResource(Res.string.title_activity_feedback),
                icon = Icons.Outlined.Feedback,
                description = stringResource(Res.string.settings_description_feedback),
                onClick = navToFeddback,
            )
            SettingItem(
                title = stringResource(Res.string.title_activity_about),
                icon = Icons.Outlined.Info,
                description = stringResource(Res.string.settings_description_about),
                onClick = navToAbout,
            )
        }
    }
}
