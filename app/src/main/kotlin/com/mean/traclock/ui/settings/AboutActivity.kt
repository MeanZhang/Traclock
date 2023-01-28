package com.mean.traclock.ui.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Code
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import coil.compose.AsyncImage
import com.mean.traclock.BuildConfig
import com.mean.traclock.R
import com.mean.traclock.ui.components.SettingGroupTitle
import com.mean.traclock.ui.components.SettingItem
import com.mean.traclock.ui.theme.TraclockTheme
import com.mean.traclock.utils.Constants.HORIZONTAL_MARGIN

class AboutActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            TraclockTheme {
                val state = rememberTopAppBarState()
                val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(state)
                Scaffold(
                    topBar = {
                        TopAppBar(
                            navigationIcon = {
                                IconButton(onClick = { finish() }) {
                                    Icon(Icons.Filled.ArrowBack, getString(R.string.back))
                                }
                            },
                            title = { Text(text = getString(R.string.about)) },
                            scrollBehavior = scrollBehavior
                        )
                    },
                    modifier = Modifier
                        .nestedScroll(scrollBehavior.nestedScrollConnection)
                ) {
                    Column(
                        Modifier
                            .verticalScroll(rememberScrollState())
                            .padding(it)
                            .padding(WindowInsets.navigationBars.asPaddingValues())
                    ) {
                        AsyncImage(
                            R.drawable.ic_logo,
                            contentDescription = stringResource(R.string.app_name),
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(vertical = 24.dp)
                                .size(72.dp)
                                .fillMaxWidth(),
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
                        )
                        Text(
                            stringResource(R.string.app_name),
                            style = MaterialTheme.typography.titleLarge,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Text(
                            stringResource(R.string.version) + BuildConfig.VERSION_NAME,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                        SettingGroupTitle(stringResource(R.string.developer))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clickable {
                                    val uri = Uri.parse(getString(R.string.github_page))
                                    val intent = Intent(Intent.ACTION_VIEW, uri)
                                    startActivity(intent)
                                }
                                .padding(HORIZONTAL_MARGIN, 12.dp)
                                .fillMaxWidth()
                        ) {
                            Image(
                                painterResource(R.drawable.avatar),
                                stringResource(R.string.developer_avatar),
                                modifier = Modifier
                                    .padding(end = 10.dp)
                                    .height(42.dp)
                                    .clip(CircleShape)
                            )
                            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                Text("Mean", style = MaterialTheme.typography.titleLarge)
                                Text(
                                    stringResource(R.string.developer_introduction),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        SettingGroupTitle(stringResource(R.string.others))
                        SettingItem(
                            icon = Icons.Default.Code,
                            title = stringResource(R.string.title_activity_open_source_licenses),
                            onClick = {
                                val intent = Intent(
                                    this@AboutActivity,
                                    OpenSourceLicensesActivity::class.java
                                )
                                startActivity(intent)
                            }
                        )
                    }
                }
            }
        }
    }
}
