package ui.components

import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

data class License(val name: String, val url: String, val license: String)

@Composable
fun LicenseItem(
//    context: Context?,
    license: License,
    modifier: Modifier = Modifier,
) {
    ListItem(
        headlineContent = { Text(license.name) },
        supportingContent = { Text(license.url + "\n" + license.license) },
//        modifier = modifier.clickable { context?.openURL(license.url) },
    )
}
