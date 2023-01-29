package com.mean.traclock.ui.settings

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Feedback
import androidx.compose.material.icons.outlined.HelpOutline
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.core.view.WindowCompat
import com.mean.traclock.R
import com.mean.traclock.ui.components.SettingGroupTitle
import com.mean.traclock.ui.components.SettingItem
import com.mean.traclock.ui.theme.TraclockTheme
import com.mean.traclock.utils.openURL

class FeedbackActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            TraclockTheme {
                val state = rememberTopAppBarState()
                val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(state)
                Scaffold(
                    modifier = Modifier
                        .nestedScroll(scrollBehavior.nestedScrollConnection),
                    topBar = {
                        TopAppBar(
                            title = { Text(text = stringResource(R.string.title_activity_feedback)) },
                            navigationIcon = {
                                IconButton(onClick = { finish() }) {
                                    Icon(Icons.Filled.ArrowBack, getString(R.string.back))
                                }
                            },
                            scrollBehavior = scrollBehavior
                        )
                    }
                ) {
                    Column(Modifier.padding(it)) {
                        SettingGroupTitle(stringResource(R.string.help))
                        SettingItem(
                            icon = Icons.Outlined.HelpOutline,
                            title = stringResource(R.string.help),
                            onClick = {
                                openURL(getString(R.string.doc_url))
                            }
                        )

                        Divider()
                        SettingGroupTitle(stringResource(R.string.feedback))
                        SettingItem(
                            icon = Icons.Outlined.Feedback,
                            title = stringResource(R.string.feedback),
                            onClick = {
                                openURL(getString(R.string.feedback_url))
                            }
                        )
                    }
                }
            }
        }
    }
}
