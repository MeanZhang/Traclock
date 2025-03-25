package com.mean.traclock.data.model

import com.mean.traclock.database.model.ProjectDurationEntry
import com.mean.traclock.model.ProjectDuration

internal fun ProjectDuration.asEntry() =
    ProjectDurationEntry(
        project = project.asEntry(),
        duration = duration,
    )
