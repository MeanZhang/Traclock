package com.mean.traclock.settings.ui

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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import com.mean.traclock.CommonRes
import com.mean.traclock.settings.Res
import com.mean.traclock.utils.openUrl
import com.mikepenz.aboutlibraries.Libs
import dev.icerock.moko.resources.compose.readTextAsState
import dev.icerock.moko.resources.compose.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun OpenSourceLicenses(
    modifier: Modifier = Modifier,
    navBack: () -> Unit,
) {
    val aboutLibsJson by Res.files.aboutlibraries_json.readTextAsState()
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
                title = { Text(stringResource(CommonRes.strings.title_activity_open_source_licenses)) },
                scrollBehavior = scrollBehavior,
            )
        },
        modifier =
            modifier
                .nestedScroll(scrollBehavior.nestedScrollConnection),
    ) { contentPadding ->
        aboutLibsJson?.let {
            val libs =
                Libs.Builder()
                    .withJson(it)
                    .build()
            val libraries = libs.libraries.filter { !it.name.startsWith("$") }.sortedBy { it.name.lowercase() }
            LazyColumn(
                contentPadding = contentPadding,
                modifier = Modifier.fillMaxWidth(),
            ) {
                items(libraries) {
                    ListItem(
                        headlineContent = { Text(it.name) },
                        supportingContent = { Text(it.website + "\n" + it.licenses.first().name) },
                        modifier =
                            modifier.clickable {
                                it.website?.let { url -> openUrl(url) }
                            },
                    )
                }
            }
        }
    }
}
