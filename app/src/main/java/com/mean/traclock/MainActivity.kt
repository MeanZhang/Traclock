package com.mean.traclock

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material.icons.outlined.Assignment
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Timeline
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.core.view.WindowCompat
import com.mean.traclock.test.TestActivity
import com.mean.traclock.ui.BottomNavType
import com.mean.traclock.ui.EditProjectActivity
import com.mean.traclock.ui.screens.Projects
import com.mean.traclock.ui.screens.Settings
import com.mean.traclock.ui.screens.Statistics
import com.mean.traclock.ui.screens.TimeLine
import com.mean.traclock.ui.theme.TraclockTheme
import com.mean.traclock.viewmodels.MainViewModel
import kotlinx.coroutines.DelicateCoroutinesApi

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    @OptIn(
        ExperimentalFoundationApi::class,
        ExperimentalMaterial3Api::class,
        DelicateCoroutinesApi::class
    )
    @SuppressLint("UnrememberedMutableState")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            TraclockTheme {
//                val systemUiController = rememberSystemUiController()
//                    systemUiController.setSystemBarsColor(Color.Transparent)
//                    systemUiController.systemBarsDarkContentEnabled = !isSystemInDarkTheme()

                val homeScreenState by viewModel.homeScreenState.collectAsState(BottomNavType.TIMELINE)

                val detailView = mutableStateOf(true)

                val scrollBehavior = remember { TopAppBarDefaults.pinnedScrollBehavior() }

//                    val coroutineScope = rememberCoroutineScope()

//                    val timelineListState = rememberLazyListState()

                Scaffold(
                    modifier = Modifier
                        .systemBarsPadding()
                        .nestedScroll(scrollBehavior.nestedScrollConnection),
                    topBar = {
                        TopBar(
                            homeScreenState,
                            detailView,
                            scrollBehavior,
//                                coroutineScope,
//                                timelineListState
                        )
                    },
                    bottomBar = { BottomBar(homeScreenState) }
                ) { contentPadding ->

                    when (homeScreenState) {
                        BottomNavType.TIMELINE -> if (detailView.value) TimeLine(
                            this,
                            viewModel.records,
                            viewModel.timeByDate,
                            true,
                            contentPadding = contentPadding
                        )
                        else TimeLine(
                            this,
                            viewModel.projectsTimeByDate,
                            viewModel.timeByDate,
                            false,
                            contentPadding = contentPadding
                        )
                        BottomNavType.PROJECTS -> Projects(
                            this,
                            viewModel.projectsTime,
                            contentPadding = contentPadding
                        )
                        BottomNavType.SETTINGS -> Settings(this, contentPadding)
                        BottomNavType.STATISTICS -> Statistics()
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun TopBar(
        homeScreenState: BottomNavType,
        detailView: MutableState<Boolean>,
        scrollBehavior: TopAppBarScrollBehavior,
//        coroutineScope: CoroutineScope,
//        listState: LazyListState
    ) {
        SmallTopAppBar(
//            modifier = Modifier.combinedClickable(
//                onClick = {},
//                onDoubleClick = {
//                    coroutineScope.launch {
//                        listState.animateScrollToItem(index = 0)
//                    }
//                }),
            title = { Text(getTitle(homeScreenState)) },
            scrollBehavior = scrollBehavior,
            actions = {
                when (homeScreenState) {
                    BottomNavType.PROJECTS -> IconButton(onClick = {
                        startActivity(
                            Intent(
                                this@MainActivity,
                                EditProjectActivity::class.java
                            )
                        )
                    }) {
                        Icon(Icons.Default.Add, stringResource(R.string.new_project))
                    }
                    BottomNavType.TIMELINE -> IconButton(onClick = { detailView.value = !detailView.value }) {
                        Icon(Icons.Default.SwapHoriz, stringResource(R.string.change_view))
                    }
                    BottomNavType.STATISTICS -> {
                    }
                    BottomNavType.SETTINGS -> IconButton(onClick = {
                        startActivity(
                            Intent(
                                this@MainActivity,
                                TestActivity::class.java
                            )
                        )
                    }) {
                        Icon(Icons.Default.MoreHoriz, "test")
                    }
                }
            }
        )
    }

    @Composable
    fun BottomBar(homeScreenState: BottomNavType) {
        NavigationBar {
            NavigationBarItem(
                selected = homeScreenState == BottomNavType.TIMELINE,
                onClick = { viewModel.setHomeScreenState(BottomNavType.TIMELINE) },
                icon = { Icon(Icons.Outlined.Timeline, getTitle(BottomNavType.TIMELINE)) },
                label = { Text(getTitle(BottomNavType.TIMELINE)) },
                alwaysShowLabel = false
            )
            NavigationBarItem(
                selected = homeScreenState == BottomNavType.PROJECTS,
                onClick = { viewModel.setHomeScreenState(BottomNavType.PROJECTS) },
                icon = { Icon(Icons.Outlined.Assignment, getTitle(BottomNavType.PROJECTS)) },
                label = { Text(getTitle(BottomNavType.PROJECTS)) },
                alwaysShowLabel = false
            )
            NavigationBarItem(
                selected = homeScreenState == BottomNavType.STATISTICS,
                onClick = { viewModel.setHomeScreenState(BottomNavType.STATISTICS) },
                icon = { Icon(Icons.Outlined.Analytics, getTitle(BottomNavType.STATISTICS)) },
                label = { Text(getTitle(BottomNavType.STATISTICS)) },
                alwaysShowLabel = false
            )
            NavigationBarItem(
                selected = homeScreenState == BottomNavType.SETTINGS,
                onClick = { viewModel.setHomeScreenState(BottomNavType.SETTINGS) },
                icon = { Icon(Icons.Outlined.Settings, getTitle(BottomNavType.SETTINGS)) },
                label = { Text(getTitle(BottomNavType.SETTINGS)) },
                alwaysShowLabel = false
            )
        }
    }

    private fun getTitle(homeScreenState: BottomNavType) = when (homeScreenState) {
        BottomNavType.TIMELINE -> this.getString(R.string.timeline)
        BottomNavType.PROJECTS -> this.getString(R.string.projects)
        BottomNavType.STATISTICS -> this.getString(R.string.statistics)
        BottomNavType.SETTINGS -> this.getString(R.string.settings)
    }
}
