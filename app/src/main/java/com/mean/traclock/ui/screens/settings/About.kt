package com.mean.traclock.ui.screens.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.mean.traclock.BuildConfig
import com.mean.traclock.R
import com.mean.traclock.ui.components.SettingGroupTitle
import com.mean.traclock.ui.components.SettingItem
import com.mean.traclock.utils.openURL

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun About(navBack: () -> Unit, navToOpenSourceLicenses: () -> Unit) {
    val context = LocalContext.current
    val state = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(state)
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = navBack) {
                        Icon(Icons.Filled.ArrowBack, stringResource(R.string.back))
                    }
                },
                title = { Text(text = stringResource(R.string.about)) },
                scrollBehavior = scrollBehavior,
            )
        },
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
    ) {
        Column(
            Modifier
                .verticalScroll(rememberScrollState())
                .padding(it)
                .padding(WindowInsets.navigationBars.asPaddingValues()),
        ) {
            AsyncImage(
                R.drawable.ic_logo,
                contentDescription = stringResource(R.string.app_name),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(vertical = 24.dp)
                    .size(72.dp)
                    .fillMaxWidth(),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
            )
            Text(
                stringResource(R.string.app_name),
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
            Text(
                stringResource(R.string.version) + BuildConfig.VERSION_NAME,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
            SettingGroupTitle(stringResource(R.string.developer))
            ListItem(
                headlineContent = { Text("Mean") },
                supportingContent = { Text(stringResource(R.string.developer_introduction)) },
                leadingContent = {
                    Image(
                        painterResource(R.drawable.avatar),
                        "开发者头像",
                        modifier = Modifier
                            .height(24.dp)
                            .clip(CircleShape),
                    )
                },
                modifier = Modifier.clickable { context.openURL(context.getString(R.string.github_page)) },
            )

            Divider()
            SettingGroupTitle(stringResource(R.string.others))
            SettingItem(
                icon = Icons.Default.Code,
                title = stringResource(R.string.title_activity_open_source_licenses),
                onClick = navToOpenSourceLicenses,
            )
        }
    }
}
