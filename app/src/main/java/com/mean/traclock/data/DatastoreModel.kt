package com.mean.traclock.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.dataStore by preferencesDataStore("traclock")

class DatastoreModel @Inject constructor(@ApplicationContext appContext: Context) {

    private val dataStore = appContext.dataStore

    private object PreferencesKeys {
        val IP = stringPreferencesKey("ip")
    }

    suspend fun setIp(ip: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.IP] = ip
        }
    }

    val ipFlow: Flow<String> = dataStore.data.map { preferences ->
        preferences[PreferencesKeys.IP] ?: "192.168.0.232"
    }
}
