package com.mean.traclock.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mean.traclock.database.Record

@Suppress("UNCHECKED_CAST")
class EditRecordViewModelFactory(private val record: Record) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return EditRecordViewModel(record) as T
    }
}