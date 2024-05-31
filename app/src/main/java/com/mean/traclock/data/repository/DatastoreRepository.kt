package com.mean.traclock.data.repository

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.mean.traclock.data.Timer
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore("traclock")

@Singleton
class DatastoreRepository
    @Inject
    constructor(
        @ApplicationContext appContext: Context,
    ) {
        private val dataStore = appContext.dataStore

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
                if (isTiming != null && timingProjectId != null && timingStartTime != null && timingStopTime != null) {
                    Timer(
                        timingProjectId,
                        timingStartTime,
                    ).apply {
                        if (!isTiming) {
                            stop(stopTime)
                        }
                    }
                } else {
                    null
                }
            }

        suspend fun saveTimer(timer: Timer) {
            dataStore.edit { preferences ->
                preferences[PreferencesKeys.IS_TIMING] = timer.isRunning
                preferences[PreferencesKeys.TIMING_PROJECT_ID] = timer.projectId
                preferences[PreferencesKeys.TIMING_START_TIME] = timer.startTime
                preferences[PreferencesKeys.TIMING_STOP_TIME] = timer.stopTime
            }
        }

        suspend fun stopTimer(timer: Timer) {
            dataStore.edit { preferences ->
                preferences[PreferencesKeys.IS_TIMING] = false
                preferences[PreferencesKeys.TIMING_STOP_TIME] = timer.stopTime
            }
        }
    }
