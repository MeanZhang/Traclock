package com.mean.traclock.ui.screens.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import com.mean.traclock.utils.Utils.openUrl
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import traclock.composeapp.generated.resources.Res
import traclock.composeapp.generated.resources.back
import traclock.composeapp.generated.resources.title_activity_open_source_licenses

@OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
@Composable
fun OpenSourceLicenses(
    modifier: Modifier = Modifier,
    navBack: () -> Unit,
) {
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
                title = { Text(stringResource(Res.string.title_activity_open_source_licenses)) },
                scrollBehavior = scrollBehavior,
            )
        },
        modifier =
            modifier
                .nestedScroll(scrollBehavior.nestedScrollConnection),
    ) { contentPadding ->
        LazyColumn(
            contentPadding = contentPadding,
            modifier = Modifier.fillMaxWidth(),
        ) {
            items(LICENSES, key = { it.name }) {
                ListItem(
                    headlineContent = { Text(it.name) },
                    supportingContent = { Text(it.url + "\n" + it.license) },
                    modifier =
                        modifier.clickable {
                            openUrl(it.url)
                        },
                )
            }
        }
    }
}

private val LICENSES =
    setOf(
        License(
            "Accompanist",
            "https://github.com/google/accompanist",
            "Apache License 2.0",
        ),
        License(
            "Android Jetpack",
            "https://github.com/androidx/androidx",
            "Apache License 2.0",
        ),
        License(
            "Coil",
            "https://github.com/coil-kt/coil",
            "Apache License 2.0",
        ),
        License(
            "Compose Multiplatform",
            "https://www.jetbrains.com/lp/compose-multiplatform/",
            "Apache License 2.0",
        ),
        License(
            "Dotenv Gradle",
            "https://github.com/uzzu/dotenv-gradle",
            "Apache License 2.0",
        ),
        License(
            "FileKit",
            "https://github.com/vinceglb/FileKit",
            "MIT License",
        ),
        License(
            "Kermit",
            "https://kermit.touchlab.co/",
            "Apache License 2.0",
        ),
        License(
            "Koala Plot",
            "https://github.com/KoalaPlot/koalaplot-core",
            "MIT License",
        ),
        License(
            "Kotlin",
            "https://github.com/JetBrains/kotlin",
            "Apache License 2.0",
        ),
        License(
            "Kotlin Symbol Processing",
            "https://github.com/google/ksp",
            "Apache License 2.0",
        ),
        License(
            "kotlinx-datetime",
            "https://github.com/Kotlin/kotlinx-datetime",
            "Apache License 2.0",
        ),
        License(
            "ktlint",
            "https://pinterest.github.io/ktlint",
            "MIT License",
        ),
        License(
            "Material Components for Android",
            "https://github.com/material-components/material-components-android",
            "Apache License 2.0",
        ),
        License(
            "Spotless",
            "https://github.com/diffplug/spotless",
            "Apache License 2.0",
        ),
        License(
            "Vico",
            "https://github.com/patrykandpatrick/vico",
            "Apache License 2.0",
        ),
    ).sortedBy { it.name.lowercase() }

private data class License(val name: String, val url: String, val license: String)
