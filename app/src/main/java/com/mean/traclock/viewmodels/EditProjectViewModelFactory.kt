package com.mean.traclock.viewmodels

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mean.traclock.data.DataModel
import com.mean.traclock.ui.components.ProjectColor

@Suppress("UNCHECKED_CAST")
class EditProjectViewModelFactory(private val projectName: String) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return EditProjectViewModel(
            projectName,
            Color(
                DataModel.dataModel.projects[projectName] ?: ProjectColor.values()[0].color.toArgb(),
            ),
        ) as T
    }
}
