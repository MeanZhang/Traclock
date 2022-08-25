package com.mean.traclock.ui.settings

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.mean.traclock.R
import com.mean.traclock.ui.components.TopBar
import com.mean.traclock.ui.theme.TraclockTheme
import com.mean.traclock.ui.utils.SetSystemBar
import com.mean.traclock.utils.Config

class OpenSourceLicensesActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            TraclockTheme {
                SetSystemBar()
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
            .padding(horizontal = Config.HORIZONTAL_MARGIN, 12.dp)
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

private fun getLicenses(): MutableList<License> {
    val licenses = mutableListOf<License>()
    licenses.add(
        License(
            "Android Jetpack",
            "https://github.com/androidx/androidx",
            "Apache License 2.0"
        )
    )
    licenses.add(
        License(
            "Accompanist",
            "https://github.com/google/accompanist",
            "Apache License 2.0"
        )
    )
    licenses.add(
        License(
            "Kotlin",
            "https://github.com/JetBrains/kotlin",
            "Apache License 2.0"
        )
    )
    licenses.add(
        License(
            "MPAndroidChart",
            "https://github.com/PhilJay/MPAndroidChart",
            "Apache License 2.0"
        )
    )
    licenses.add(
        License(
            "LeakCanary",
            "https://github.com/square/leakcanary",
            "Apache License 2.0"
        )
    )
    licenses.add(
        License(
            "Lottie",
            "https://github.com/airbnb/lottie-android",
            "Apache License 2.0"
        )
    )
    licenses.add(
        License(
            "DateTimePicker",
            "https://github.com/loperSeven/DateTimePicker",
            "MIT License"
        )
    )
    licenses.add(
        License(
            "Material Components for Android",
            "https://github.com/material-components/material-components-android",
            "Apache License 2.0"
        )
    )
    licenses.add(
        License(
            "ThreeTen Android Backport",
            "https://github.com/JakeWharton/ThreeTenABP",
            "Apache License 2.0"
        )
    )
    licenses.add(
        License(
            "Coil",
            "https://github.com/coil-kt/coil",
            "Apache License 2.0"
        )
    )
    licenses.add(
        License(
            "XLog",
            "https://github.com/elvishew/xLog",
            "Apache License 2.0"
        )
    )
    licenses.sortBy { it.name }
    return licenses
}

data class License(val name: String, val url: String, val license: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Content(activity: OpenSourceLicensesActivity?) {
    val decayAnimationSpec = rememberSplineBasedDecay<Float>()
    val state = rememberTopAppBarScrollState()
    val scrollBehavior = remember(decayAnimationSpec) {
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(decayAnimationSpec, state)
    }
    Scaffold(
        topBar = {
            TopBar(
                navigationIcon = {
                    IconButton(onClick = { activity?.finish() }) {
                        Icon(Icons.Filled.ArrowBack, stringResource(R.string.back))
                    }
                },
                title = stringResource(R.string.title_activity_open_source_licenses),
                scrollBehavior = scrollBehavior
            )
        },
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { contentPadding ->
        LazyColumn(
            modifier = Modifier.padding(contentPadding),
            contentPadding = WindowInsets.navigationBars.asPaddingValues()
        ) {
            items(getLicenses()) {
                LicenseItem(activity, it)
            }
        }
    }
}

@Composable
@Preview(showSystemUi = true)
fun PreviewOSS() {
    Content(null)
}
