package com.mean.traclock.record.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.mean.traclock.record.ui.EditRecord

data class EditRecordKey(val id: Long) : NavKey

fun EntryProviderScope<NavKey>.recordEntries(navBack: () -> Unit) {
    entry<EditRecordKey> { key ->
        EditRecord(
            id = key.id,
            navBack = navBack,
        )
    }
}
