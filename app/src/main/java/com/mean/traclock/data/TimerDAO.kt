package com.mean.traclock.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.mean.traclock.App
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("timer")

/**
 * 该类封装 [TimerModel] 在 [DataStore] 中的持久化存储
 */
internal object TimerDAO {
    private val dataStore = App.context.dataStore

    private object PreferencesKeys {
        val IS_RUNNING = booleanPreferencesKey("is_running")
        val PROJECT_ID = intPreferencesKey("project_id")
        val START_TIME = longPreferencesKey("start_time")
    }

    /** 计时器的项目id */
    val projectIdFlow: Flow<Int?> = dataStore.data.map { preferences ->
        preferences[PreferencesKeys.PROJECT_ID]
    }

    /** 计时器是否正在运行 */
    val isRunningFlow: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[PreferencesKeys.IS_RUNNING] ?: false
    }

    /** 计时器开始的时间，以毫秒为单位 */
    val startTimeFlow: Flow<Long> = dataStore.data.map { preferences ->
        preferences[PreferencesKeys.START_TIME] ?: 0
    }

    /**
     * @param projectId 项目 id
     * @param isRunning 计时器是否正在运行
     * @param startTime 计时器开始的时间，以毫秒为单位
     */
    suspend fun setTimer(
        projectId: Int,
        isRunning: Boolean,
        startTime: Long,
    ) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.PROJECT_ID] = projectId
            preferences[PreferencesKeys.IS_RUNNING] = isRunning
            preferences[PreferencesKeys.START_TIME] = startTime
        }
    }
}
