package com.mean.traclock.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import com.mean.traclock.data.Timer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.internal.SynchronizedObject
import kotlinx.coroutines.internal.synchronized
import kotlinx.coroutines.withContext
import okio.Path.Companion.toPath

private lateinit var dataStore: DataStore<Preferences>

@OptIn(InternalCoroutinesApi::class)
private val lock = SynchronizedObject()

@OptIn(InternalCoroutinesApi::class)
fun getDataStore(producePath: () -> String): DataStore<Preferences> =
    synchronized(lock) {
        if (::dataStore.isInitialized) {
            dataStore
        } else {
            PreferenceDataStoreFactory.createWithPath(produceFile = { producePath().toPath() })
                .also { dataStore = it }
        }
    }

internal const val DATA_STORE_FILE_NAME = "traclock.preferences_pb"

class DatastoreRepository(
    private val dataStore: DataStore<Preferences>,
) {
    private object PreferencesKeys {
        val IS_TIMING = booleanPreferencesKey("is_timing")
        val TIMING_PROJECT_ID = longPreferencesKey("timing_project_id")
        val TIMING_START_TIME = longPreferencesKey("timing_start_time")
        val TIMING_STOP_TIME = longPreferencesKey("timing_stop_time")
    }

    val timer: Flow<Timer?> =
        dataStore.data.map { preferences ->
            val isTiming = preferences[PreferencesKeys.IS_TIMING]
            val timingProjectId = preferences[PreferencesKeys.TIMING_PROJECT_ID]
            val timingStartTime = preferences[PreferencesKeys.TIMING_START_TIME]
            val timingStopTime = preferences[PreferencesKeys.TIMING_STOP_TIME]
            if (isTiming != null && timingProjectId != null && timingStartTime != null) {
                Timer(
                    timingProjectId,
                    timingStartTime,
                ).apply {
                    if (!isTiming && timingStopTime != null) {
                        stop(timingStopTime)
                    }
                }
            } else {
                null
            }
        }

    suspend fun saveTimer(timer: Timer) {
        withContext(Dispatchers.IO) {
            dataStore.edit { preferences ->
                preferences[PreferencesKeys.IS_TIMING] = timer.isRunning
                preferences[PreferencesKeys.TIMING_PROJECT_ID] = timer.projectId
                preferences[PreferencesKeys.TIMING_START_TIME] = timer.startTime
                val stopTime = timer.stopTime
                if (stopTime != null) {
                    preferences[PreferencesKeys.TIMING_STOP_TIME] = stopTime
                } else {
                    preferences.remove(PreferencesKeys.TIMING_STOP_TIME)
                }
            }
        }
    }

    suspend fun stopTimer(timer: Timer) {
        withContext(Dispatchers.IO) {
            dataStore.edit { preferences ->
                preferences[PreferencesKeys.IS_TIMING] = false
                timer.stopTime?.let { preferences[PreferencesKeys.TIMING_STOP_TIME] = it }
            }
        }
    }
}
