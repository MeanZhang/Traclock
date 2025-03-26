package com.mean.traclock.settings.ui

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
import com.mean.traclock.CommonRes
import com.mean.traclock.Res
import com.mean.traclock.avatar
import com.mean.traclock.ui.components.SettingGroupTitle
import com.mean.traclock.ui.components.SettingItem
import com.mean.traclock.utils.getString
import com.mean.traclock.utils.openUrl
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
@Composable
internal fun About(
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
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, stringResource(CommonRes.strings.back))
                    }
                },
                title = { Text(text = stringResource(CommonRes.strings.about)) },
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
//            Image(
//                // TODO moko-resources SVG颜色显示错误
//                painter = painterResource(CommonRes.images.ic_logo),
//                contentDescription = stringResource(CommonRes.strings.app_name),
//                modifier =
//                    Modifier
//                        .align(Alignment.CenterHorizontally)
//                        .padding(vertical = 24.dp)
//                        .size(64.dp)
//                        .fillMaxWidth(),
//                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
//            )
            // TODO 桌面端无法加载SVG
            AsyncImage(
                model = Res.getUri("drawable/ic_logo.svg"),
                contentDescription = stringResource(CommonRes.strings.app_name),
                modifier =
                    Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(vertical = 24.dp)
                        .size(64.dp)
                        .fillMaxWidth(),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
            )
            Text(
                stringResource(CommonRes.strings.app_name),
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
            SettingGroupTitle(stringResource(CommonRes.strings.developer))
            ListItem(
                headlineContent = { Text("Mean") },
                supportingContent = { Text(stringResource(CommonRes.strings.developer_introduction)) },
                leadingContent = {
                    Image(
                        painter = painterResource(Res.drawable.avatar),
                        contentDescription = "开发者头像",
                        modifier =
                            Modifier
                                .height(24.dp)
                                .clip(CircleShape),
                    )
                },
                modifier =
                    Modifier.clickable {
                        scope.launch {
                            openUrl(getString(CommonRes.strings.github_page))
                        }
                    },
            )

            HorizontalDivider()
            SettingGroupTitle(stringResource(CommonRes.strings.others))
            SettingItem(
                icon = Icons.Default.Code,
                title = stringResource(CommonRes.strings.title_activity_open_source_licenses),
                onClick = navToOpenSourceLicenses,
            )
        }
    }
}
