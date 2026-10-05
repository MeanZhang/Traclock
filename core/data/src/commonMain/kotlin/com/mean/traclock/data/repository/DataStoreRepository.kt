package com.mean.traclock.data.repository

import com.mean.traclock.datastore.DataStoreSource
import com.mean.traclock.model.Timer

class DataStoreRepository(private val datastoreSource: DataStoreSource) {
    val timer = datastoreSource.timer

    suspend fun saveTimer(timer: Timer) = datastoreSource.saveTimer(timer)

    suspend fun stopTimer(timer: Timer) = datastoreSource.stopTimer(timer)
}
