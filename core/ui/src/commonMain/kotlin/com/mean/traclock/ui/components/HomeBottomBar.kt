package com.mean.traclock.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mean.traclock.ui.HomeRoute
import dev.icerock.moko.resources.compose.stringResource

@Composable
fun HomeBottomBar(
    modifier: Modifier = Modifier,
    currentRoute: String?,
    navTo: (HomeRoute) -> Unit,
) {
    AnimatedVisibility(
        visible = HomeRoute.entries.map { it.name }.contains(currentRoute),
        enter = expandVertically(),
        exit = shrinkVertically(),
    ) {
        NavigationBar(modifier = modifier.fillMaxWidth()) {
            HomeRoute.entries.forEach { destination ->
                NavigationBarItem(
                    selected = currentRoute == destination.name,
                    onClick = {
                        navTo(destination)
                    },
                    icon = { Icon(destination.icon, stringResource(destination.titleId)) },
                    label = { Text(stringResource(destination.titleId)) },
                    alwaysShowLabel = false,
                )
            }
        }
    }
}
