package com.mean.traclock.viewmodels

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mean.traclock.App

@Suppress("UNCHECKED_CAST")
class EditProjectViewModelFactory(private val projectName: String) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return EditProjectViewModel(projectName, Color(App.projectsList[projectName] ?: 0)) as T
    }
}
