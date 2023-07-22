package com.mean.traclock.ui.components

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mean.traclock.utils.openURL

data class License(val name: String, val url: String, val license: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LicenseItem(context: Context?, license: License) {
    ListItem(
        headlineContent = { Text(license.name) },
        supportingContent = { Text(license.url + "\n" + license.license) },
        modifier = Modifier.clickable { context?.openURL(license.url) },
    )
}
