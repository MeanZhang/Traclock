package com.mean.traclock.statistic.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.mean.traclock.statistic.Statistics

data object StatisticKey : NavKey

fun EntryProviderScope<NavKey>.statisticEntry() {
    entry<StatisticKey> {
        Statistics(modifier = Modifier.fillMaxSize())
    }
}
