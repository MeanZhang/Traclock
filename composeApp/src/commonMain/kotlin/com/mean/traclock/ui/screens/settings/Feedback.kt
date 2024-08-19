package com.mean.traclock.ui.screens.settings

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
import com.mean.traclock.ui.components.SettingGroupTitle
import com.mean.traclock.ui.components.SettingItem
import com.mean.traclock.utils.Utils.openUrl
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import traclock.composeapp.generated.resources.Res
import traclock.composeapp.generated.resources.back
import traclock.composeapp.generated.resources.doc_url
import traclock.composeapp.generated.resources.feedback
import traclock.composeapp.generated.resources.feedback_url
import traclock.composeapp.generated.resources.help
import traclock.composeapp.generated.resources.title_activity_feedback

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Feedback(
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
                title = { Text(text = stringResource(Res.string.title_activity_feedback)) },
                navigationIcon = {
                    IconButton(onClick = navBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, stringResource(Res.string.back))
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },
    ) {
        Column(Modifier.padding(it)) {
            SettingGroupTitle(stringResource(Res.string.help))
            SettingItem(
                icon = Icons.AutoMirrored.Outlined.HelpOutline,
                title = stringResource(Res.string.help),
                onClick = {
                    scope.launch {
                        openUrl(getString(Res.string.doc_url))
                    }
                },
            )

            HorizontalDivider()
            SettingGroupTitle(stringResource(Res.string.feedback))
            SettingItem(
                icon = Icons.Outlined.Feedback,
                title = stringResource(Res.string.feedback),
                onClick = {
                    scope.launch {
                        openUrl(getString(Res.string.feedback_url))
                    }
                },
            )
        }
    }
}
