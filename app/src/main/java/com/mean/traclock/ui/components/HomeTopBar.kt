package com.mean.traclock.ui.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.mean.traclock.ui.navigation.HomeRoute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(
    currentRoute: HomeRoute,
    scrollBehavior: TopAppBarScrollBehavior,
    actions:
        @Composable()
        (RowScope.() -> Unit) = {},
) {
    TopAppBar(
        title = { Text(stringResource(currentRoute.titleId)) },
        scrollBehavior = scrollBehavior,
        actions = actions,
    )
}
