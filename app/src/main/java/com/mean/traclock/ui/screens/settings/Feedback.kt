package com.mean.traclock.ui.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.HelpOutline
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Feedback
import androidx.compose.material.icons.outlined.HelpOutline
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.mean.traclock.R
import com.mean.traclock.ui.components.SettingGroupTitle
import com.mean.traclock.ui.components.SettingItem
import com.mean.traclock.utils.openURL

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Feedback(
    modifier: Modifier = Modifier,
    navBack: () -> Unit,
) {
    val context = LocalContext.current
    val state = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(state)
    Scaffold(
        modifier =
            modifier
                .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.title_activity_feedback)) },
                navigationIcon = {
                    IconButton(onClick = navBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, stringResource(R.string.back))
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        },
    ) {
        Column(Modifier.padding(it)) {
            SettingGroupTitle(stringResource(R.string.help))
            SettingItem(
                icon = Icons.AutoMirrored.Outlined.HelpOutline,
                title = stringResource(R.string.help),
                onClick = {
                    context.openURL(context.getString(R.string.doc_url))
                },
            )

            HorizontalDivider()
            SettingGroupTitle(stringResource(R.string.feedback))
            SettingItem(
                icon = Icons.Outlined.Feedback,
                title = stringResource(R.string.feedback),
                onClick = {
                    context.openURL(context.getString(R.string.feedback_url))
                },
            )
        }
    }
}
