package com.mean.traclock.data.model

import com.mean.traclock.database.model.RecordEntry
import com.mean.traclock.model.Record

internal fun Record.asEntry() =
    RecordEntry(
        projectId = projectId,
        startTime = startTime,
        endTime = endTime,
        date = date,
        id = id,
    )
