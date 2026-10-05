package com.mean.traclock.data.model

import com.mean.traclock.database.model.ProjectEntry
import com.mean.traclock.model.Project

internal fun Project.asEntry() =
    ProjectEntry(
        name = name,
        color = color,
        id = id,
    )
