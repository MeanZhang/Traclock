package com.mean.traclock.ui.settings

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.mean.traclock.R
import com.mean.traclock.ui.Constants
import com.mean.traclock.ui.theme.TraclockTheme

class OpenSourceLicensesActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            TraclockTheme {
                Content(this)
            }
        }
    }
}

@Composable
fun LicenseItem(context: Context?, license: License) {
    Column(
        Modifier
            .clickable {
                context?.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(license.url)))
            }
            .fillMaxWidth()
            .padding(horizontal = Constants.HORIZONTAL_MARGIN, 12.dp)
    ) {
        Text(
            license.name,
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            license.url,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            license.license,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Content(activity: OpenSourceLicensesActivity?) {
    val state = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(state)
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { activity?.finish() }) {
                        Icon(Icons.Filled.ArrowBack, stringResource(R.string.back))
                    }
                },
                title = { Text(stringResource(R.string.title_activity_open_source_licenses)) },
                scrollBehavior = scrollBehavior
            )
        },
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { contentPadding ->
        LazyColumn(
            contentPadding = contentPadding
        ) {
            items(LICENSES) {
                LicenseItem(activity, it)
            }
        }
    }
}

private val LICENSES = listOf(
    License(
        "Android Jetpack",
        "https://github.com/androidx/androidx",
        "Apache License 2.0"
    ),
    License(
        "Accompanist",
        "https://github.com/google/accompanist",
        "Apache License 2.0"
    ),
    License(
        "Kotlin",
        "https://github.com/JetBrains/kotlin",
        "Apache License 2.0"
    ),
    License(
        "MPAndroidChart",
        "https://github.com/PhilJay/MPAndroidChart",
        "Apache License 2.0"
    ),
    License(
        "LeakCanary",
        "https://github.com/square/leakcanary",
        "Apache License 2.0"
    ),
    License(
        "Lottie",
        "https://github.com/airbnb/lottie-android",
        "Apache License 2.0"
    ),
    License(
        "DateTimePicker",
        "https://github.com/loperSeven/DateTimePicker",
        "MIT License"
    ),
    License(
        "Material Components for Android",
        "https://github.com/material-components/material-components-android",
        "Apache License 2.0"
    ),
    License(
        "ThreeTen Android Backport",
        "https://github.com/JakeWharton/ThreeTenABP",
        "Apache License 2.0"
    ),
    License(
        "Coil",
        "https://github.com/coil-kt/coil",
        "Apache License 2.0"
    ),
    License(
        "XLog",
        "https://github.com/elvishew/xLog",
        "Apache License 2.0"
    )
).sortedBy { it.name }

data class License(val name: String, val url: String, val license: String)

@Composable
@Preview(showSystemUi = true)
fun PreviewOSS() {
    Content(null)
}
