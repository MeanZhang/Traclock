package com.mean.traclock.settings.ui

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
import com.mean.traclock.CommonRes
import com.mean.traclock.ui.HomeRoute
import com.mean.traclock.ui.components.HomeTopBar
import com.mean.traclock.ui.components.SettingGroupTitle
import com.mean.traclock.ui.components.SettingItem
import dev.icerock.moko.resources.compose.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun Settings(
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
            SettingGroupTitle(stringResource(CommonRes.strings.normal))

            SettingItem(
                title = stringResource(CommonRes.strings.title_activity_backup_restore),
                icon = Icons.Outlined.SettingsBackupRestore,
                description = stringResource(CommonRes.strings.settings_description_backup_restore),
                onClick = navToBackupRestore,
            )
            HorizontalDivider()
            SettingGroupTitle(stringResource(CommonRes.strings.others))
            SettingItem(
                title = stringResource(CommonRes.strings.title_activity_feedback),
                icon = Icons.Outlined.Feedback,
                description = stringResource(CommonRes.strings.settings_description_feedback),
                onClick = navToFeddback,
            )
            SettingItem(
                title = stringResource(CommonRes.strings.title_activity_about),
                icon = Icons.Outlined.Info,
                description = stringResource(CommonRes.strings.settings_description_about),
                onClick = navToAbout,
            )
        }
    }
}
