package com.mean.traclock

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material.icons.outlined.Assignment
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Timeline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.core.view.WindowCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.mean.traclock.test.TestActivity
import com.mean.traclock.ui.BottomNavType
import com.mean.traclock.ui.BottomNavType.*
import com.mean.traclock.ui.EditProjectActivity
import com.mean.traclock.ui.components.ComingSoon
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

                val homeScreenState by viewModel.homeScreenState.observeAsState(TIMELINE)

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
                        TIMELINE -> if (detailView.value) TimeLine(
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
                        PROJECTS -> Projects(
                            this,
                            viewModel.projectsTime,
                            contentPadding = contentPadding
                        )
                        SETTINGS -> Settings(this, contentPadding)
                        STATISTICS-> Statistics()
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
                    PROJECTS -> IconButton(onClick = {
                        startActivity(
                            Intent(
                                this@MainActivity,
                                EditProjectActivity::class.java
                            )
                        )
                    }) {
                        Icon(Icons.Default.Add, stringResource(R.string.new_project))
                    }
                    TIMELINE -> IconButton(onClick = { detailView.value = !detailView.value }) {
                        Icon(Icons.Default.SwapHoriz, stringResource(R.string.change_view))
                    }
                    STATISTICS -> {
                    }
                    SETTINGS -> IconButton(onClick = {
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
                selected = homeScreenState == TIMELINE,
                onClick = { viewModel.setHomeScreenState(TIMELINE) },
                icon = { Icon(Icons.Outlined.Timeline, getTitle(TIMELINE)) },
                label = { Text(getTitle(TIMELINE)) },
                alwaysShowLabel = false
            )
            NavigationBarItem(
                selected = homeScreenState == PROJECTS,
                onClick = { viewModel.setHomeScreenState(PROJECTS) },
                icon = { Icon(Icons.Outlined.Assignment, getTitle(PROJECTS)) },
                label = { Text(getTitle(PROJECTS)) },
                alwaysShowLabel = false
            )
            NavigationBarItem(
                selected = homeScreenState == STATISTICS,
                onClick = { viewModel.setHomeScreenState(STATISTICS) },
                icon = { Icon(Icons.Outlined.Analytics, getTitle(STATISTICS)) },
                label = { Text(getTitle(STATISTICS)) },
                alwaysShowLabel = false
            )
            NavigationBarItem(
                selected = homeScreenState == SETTINGS,
                onClick = { viewModel.setHomeScreenState(SETTINGS) },
                icon = { Icon(Icons.Outlined.Settings, getTitle(SETTINGS)) },
                label = { Text(getTitle(SETTINGS)) },
                alwaysShowLabel = false
            )
        }
    }

    private fun getTitle(homeScreenState: BottomNavType) = when (homeScreenState) {
        TIMELINE -> this.getString(R.string.timeline)
        PROJECTS -> this.getString(R.string.projects)
        STATISTICS -> this.getString(R.string.statistics)
        SETTINGS -> this.getString(R.string.settings)
    }
}