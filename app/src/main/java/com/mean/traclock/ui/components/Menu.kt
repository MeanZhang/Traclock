package com.mean.traclock.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import com.mean.traclock.TraclockApplication

@Composable
fun TopbarMenu(
    showMenu: MutableState<Boolean>,
    align: Alignment = Alignment.TopEnd,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    content: @Composable ColumnScope.() -> Unit
) {
    Box(
        Modifier
            .padding(contentPadding)
            .padding(horizontal = TraclockApplication.horizontalMargin)
            .fillMaxSize()
            .wrapContentSize(align = align)
    ) {
        Menu(showMenu = showMenu, content = content)
    }
}

@Composable
fun Menu(showMenu: MutableState<Boolean>, content: @Composable ColumnScope.() -> Unit) {
    DropdownMenu(
        expanded = showMenu.value,
        onDismissRequest = { showMenu.value = false },
        modifier = Modifier.background(MaterialTheme.colorScheme.surfaceVariant),
        content = content
    )
}

@Composable
fun MenuItem(
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    DropdownMenuItem(
        onClick = onClick,
        content = {
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant,
                content = content
            )
        })
}