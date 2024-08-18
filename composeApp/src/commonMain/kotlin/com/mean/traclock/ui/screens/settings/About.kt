package com.mean.traclock.ui.screens.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Code
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.mean.traclock.ui.components.SettingGroupTitle
import com.mean.traclock.ui.components.SettingItem
import com.mean.traclock.utils.Utils.openUrl
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import traclock.composeapp.generated.resources.Res
import traclock.composeapp.generated.resources.about
import traclock.composeapp.generated.resources.app_name
import traclock.composeapp.generated.resources.avatar
import traclock.composeapp.generated.resources.back
import traclock.composeapp.generated.resources.developer
import traclock.composeapp.generated.resources.developer_introduction
import traclock.composeapp.generated.resources.github_page
import traclock.composeapp.generated.resources.ic_logo
import traclock.composeapp.generated.resources.others
import traclock.composeapp.generated.resources.title_activity_open_source_licenses

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun About(
    navBack: () -> Unit,
    modifier: Modifier = Modifier,
    navToOpenSourceLicenses: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val state = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(state)
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = navBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, stringResource(Res.string.back))
                    }
                },
                title = { Text(text = stringResource(Res.string.about)) },
                scrollBehavior = scrollBehavior,
            )
        },
        modifier =
            modifier
                .nestedScroll(scrollBehavior.nestedScrollConnection),
    ) {
        Column(
            Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
                .padding(it)
                .padding(WindowInsets.navigationBars.asPaddingValues()),
        ) {
            AsyncImage(
                model = Res.drawable.ic_logo,
                contentDescription = stringResource(Res.string.app_name),
                modifier =
                    Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(vertical = 24.dp)
                        .size(72.dp)
                        .fillMaxWidth(),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
            )
            Text(
                stringResource(Res.string.app_name),
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
//            Text(
//                stringResource(Res.string.version) + BuildConfig.VERSION_NAME,
//                textAlign = TextAlign.Center,
//                modifier = Modifier.fillMaxWidth(),
//            )
            SettingGroupTitle(stringResource(Res.string.developer))
            ListItem(
                headlineContent = { Text("Mean") },
                supportingContent = { Text(stringResource(Res.string.developer_introduction)) },
                leadingContent = {
                    Image(
                        painterResource(Res.drawable.avatar),
                        "开发者头像",
                        modifier =
                            Modifier
                                .height(24.dp)
                                .clip(CircleShape),
                    )
                },
                modifier =
                    Modifier.clickable {
                        scope.launch {
                            openUrl(getString(Res.string.github_page))
                        }
                    },
            )

            HorizontalDivider()
            SettingGroupTitle(stringResource(Res.string.others))
            SettingItem(
                icon = Icons.Default.Code,
                title = stringResource(Res.string.title_activity_open_source_licenses),
                onClick = navToOpenSourceLicenses,
            )
        }
    }
}
