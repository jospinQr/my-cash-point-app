package org.megamind.mycashpoint.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.firstOrNull

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class DataStorageManager(private val context: Context) {

    companion object {
        private val TOKEN_KEY = stringPreferencesKey("token")
    }

    suspend fun getToken(): String? {
        return context.dataStore.data.firstOrNull()?.get(TOKEN_KEY)
    }

    suspend fun saveToken(token: String) {
        context.dataStore.edit { it[TOKEN_KEY] = token }
    }


}
