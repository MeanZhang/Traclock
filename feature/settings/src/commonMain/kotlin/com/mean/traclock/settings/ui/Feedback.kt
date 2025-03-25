package com.mean.traclock.settings.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.HelpOutline
import androidx.compose.material.icons.outlined.Feedback
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import com.mean.traclock.CommonRes
import com.mean.traclock.ui.components.SettingGroupTitle
import com.mean.traclock.ui.components.SettingItem
import com.mean.traclock.utils.getString
import com.mean.traclock.utils.openUrl
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun Feedback(
    modifier: Modifier = Modifier,
    navBack: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val state = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(state)
    Scaffold(
        modifier =
            modifier
                .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(CommonRes.strings.title_activity_feedback)) },
                navigationIcon = {
                    IconButton(onClick = navBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, stringResource(CommonRes.strings.back))
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },
    ) {
        Column(Modifier.padding(it)) {
            SettingGroupTitle(stringResource(CommonRes.strings.help))
            SettingItem(
                icon = Icons.AutoMirrored.Outlined.HelpOutline,
                title = stringResource(CommonRes.strings.help),
                onClick = {
                    scope.launch {
                        openUrl(getString(CommonRes.strings.doc_url))
                    }
                },
            )

            HorizontalDivider()
            SettingGroupTitle(stringResource(CommonRes.strings.feedback))
            SettingItem(
                icon = Icons.Outlined.Feedback,
                title = stringResource(CommonRes.strings.feedback),
                onClick = {
                    scope.launch {
                        openUrl(getString(CommonRes.strings.feedback_url))
                    }
                },
            )
        }
    }
}
