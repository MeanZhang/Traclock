package com.mean.traclock.ui.navigation

import androidx.navigation3.runtime.NavKey
import com.mean.traclock.home.navigation.ProjectsKey
import com.mean.traclock.home.navigation.TimelineKey
import com.mean.traclock.settings.navigation.SettingsKey
import com.mean.traclock.statistic.navigation.StatisticKey
import com.mean.traclock.ui.HomeRoute

/** 底部导航栏只对应栈顶为首页 tab 的情况，其余页面返回 null 以隐藏底栏 */
fun NavKey.toHomeRoute(): HomeRoute? =
    when (this) {
        TimelineKey -> HomeRoute.TIMELINE
        ProjectsKey -> HomeRoute.PROJECTS
        StatisticKey -> HomeRoute.STATISTICS
        SettingsKey -> HomeRoute.SETTINGS
        else -> null
    }

fun HomeRoute.toNavKey(): NavKey =
    when (this) {
        HomeRoute.TIMELINE -> TimelineKey
        HomeRoute.PROJECTS -> ProjectsKey
        HomeRoute.STATISTICS -> StatisticKey
        HomeRoute.SETTINGS -> SettingsKey
    }
