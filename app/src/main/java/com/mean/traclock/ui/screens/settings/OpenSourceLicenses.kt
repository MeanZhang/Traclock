package com.mean.traclock.ui.screens.settings

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.mean.traclock.R
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OpenSourceLicenses(
    modifier: Modifier = Modifier,
    navBack: () -> Unit,
) {
    val state = rememberTopAppBarState()
    val context = LocalContext.current
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(state)
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = navBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, stringResource(R.string.back))
                    }
                },
                title = { Text(stringResource(R.string.title_activity_open_source_licenses)) },
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
                            context.startActivity(
                                Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse(it.url),
                                ),
                            )
                        },
                )
            }
        }
    }
}

private val LICENSES =
    listOf(
        License(
            "Android Jetpack",
            "https://github.com/androidx/androidx",
            "Apache License 2.0",
        ),
        License(
            "Accompanist",
            "https://github.com/google/accompanist",
            "Apache License 2.0",
        ),
        License(
            "Kotlin",
            "https://github.com/JetBrains/kotlin",
            "Apache License 2.0",
        ),
        License(
            "MPAndroidChart",
            "https://github.com/PhilJay/MPAndroidChart",
            "Apache License 2.0",
        ),
        License(
            "LeakCanary",
            "https://github.com/square/leakcanary",
            "Apache License 2.0",
        ),
        License(
            "Lottie",
            "https://github.com/airbnb/lottie-android",
            "Apache License 2.0",
        ),
        License(
            "DateTimePicker",
            "https://github.com/loperSeven/DateTimePicker",
            "MIT License",
        ),
        License(
            "Material Components for Android",
            "https://github.com/material-components/material-components-android",
            "Apache License 2.0",
        ),
        License(
            "Coil",
            "https://github.com/coil-kt/coil",
            "Apache License 2.0",
        ),
        License(
            "XLog",
            "https://github.com/elvishew/xLog",
            "Apache License 2.0",
        ),
        License(
            "Spotless",
            "https://github.com/diffplug/spotless",
            "Apache License 2.0",
        ),
        License(
            "ktlint",
            "https://pinterest.github.io/ktlint",
            "MIT License",
        ),
        License(
            "Kotlin Symbol Processing",
            "https://github.com/google/ksp",
            "Apache License 2.0",
        ),
        License(
            "Dotenv Gradle",
            "https://github.com/uzzu/dotenv-gradle",
            "Apache License 2.0",
        ),
        License(
            "Hilt",
            "https://dagger.dev/hilt",
            "Apache License 2.0",
        ),
        License(
            "Glance",
            "https://github.com/guolindev/Glance",
            "Apache License 2.0",
        ),
    ).sortedBy { it.name.lowercase(Locale.getDefault()) }

private data class License(val name: String, val url: String, val license: String)
