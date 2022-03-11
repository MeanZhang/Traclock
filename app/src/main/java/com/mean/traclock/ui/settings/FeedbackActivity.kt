package com.mean.traclock.ui.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Feedback
import androidx.compose.material.icons.outlined.HelpOutline
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import com.mean.traclock.R
import com.mean.traclock.ui.components.SettingGroupTitle
import com.mean.traclock.ui.components.SettingItem
import com.mean.traclock.ui.theme.TraclockTheme
import com.mean.traclock.ui.util.SetSystemBar

class FeedbackActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TraclockTheme {
                SetSystemBar()
                val scrollBehavior = remember { TopAppBarDefaults.pinnedScrollBehavior() }
                Scaffold(
                    modifier = Modifier
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
                    Column(Modifier.navigationBarsPadding()) {
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
