package com.mean.traclock.ui.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Feedback
import androidx.compose.material.icons.outlined.HelpOutline
import androidx.compose.material3.*
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import com.mean.traclock.R
import com.mean.traclock.ui.components.SettingGroupTitle
import com.mean.traclock.ui.components.SettingItem
import com.mean.traclock.ui.theme.TraclockTheme

class FeedbackActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TraclockTheme {
                val scrollBehavior = remember { TopAppBarDefaults.pinnedScrollBehavior() }
                Scaffold(
                    modifier = Modifier
                        .systemBarsPadding()
                        .nestedScroll(scrollBehavior.nestedScrollConnection),
                    topBar = {
                        SmallTopAppBar(
                            title = { Text(stringResource(R.string.title_activity_feedback)) },
                            navigationIcon = {
                                IconButton(onClick = { finish() }) {
                                    Icon(Icons.Filled.ArrowBack, getString(R.string.back))
                                }
                            },
                            scrollBehavior = scrollBehavior
                        )
                    }
                ) {
                    Column() {
                        SettingGroupTitle(stringResource(R.string.help))
                        SettingItem(
                            icon = Icons.Outlined.HelpOutline,
                            title = stringResource(R.string.help),
                            description = stringResource(R.string.help),
                            onClick = {
                                val uri = Uri.parse(getString(R.string.doc_url))
                                val intent = Intent(Intent.ACTION_VIEW, uri)
                                startActivity(intent)
                            }
                        )

                        Divider()

                        SettingGroupTitle(stringResource(R.string.feedback))
                        SettingItem(
                            icon = Icons.Outlined.Feedback,
                            title = stringResource(R.string.feedback),
                            description = stringResource(R.string.feedback),
                            onClick = {
                                val uri = Uri.parse(getString(R.string.feedback_url))
                                val intent = Intent(Intent.ACTION_VIEW, uri)
                                startActivity(intent)
                            }
                        )
                    }
                }
            }
        }
    }
}
