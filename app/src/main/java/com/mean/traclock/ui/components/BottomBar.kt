package com.mean.traclock.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier

@Composable
fun BottomBar(content: @Composable RowScope.() -> Unit) {
    val topBarColors = TopAppBarDefaults.smallTopAppBarColors()
    val navBarColor by topBarColors.containerColor(scrollFraction = 1f)

    Column {
        NavigationBar(content = content)
        Spacer(
            modifier = Modifier
                .windowInsetsBottomHeight(WindowInsets.navigationBars)
                .fillMaxWidth()
                .background(navBarColor)
        )
    }
}
