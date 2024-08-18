package ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import com.mean.traclock.ui.navigation.HomeRoute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScaffold(
    modifier: Modifier = Modifier,
    route: HomeRoute,
    actions: @Composable RowScope.() -> Unit = {},
    floatingActionButton: @Composable (() -> Unit) = {},
    content: @Composable (PaddingValues) -> Unit,
) {
    val state = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(state)
    Scaffold(
        topBar = {
            HomeTopBar(
                currentRoute = route,
                scrollBehavior = scrollBehavior,
                actions = actions,
            )
        },
        floatingActionButton = floatingActionButton,
        modifier =
            modifier
                .nestedScroll(scrollBehavior.nestedScrollConnection),
    ) { contentPadding ->
        val layoutDirection = LocalLayoutDirection.current
        val padding =
            PaddingValues(
                top = contentPadding.calculateTopPadding(),
                bottom = 0.dp,
                start = contentPadding.calculateStartPadding(layoutDirection),
                end = contentPadding.calculateEndPadding(layoutDirection),
            )
        content(padding)
    }
}
