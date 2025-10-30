package org.megamind.mycashpoint.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class DataStorageManager(private val context: Context) {

    companion object {
        private val USERNAME_KEY = stringPreferencesKey("username")
        private val USERID_KEY = intPreferencesKey("userId")
    }

    suspend fun getUsername(): String? {
        return context.dataStore.data.firstOrNull()?.get(USERNAME_KEY)
    }

    suspend fun saveUsername(username: String) {
        context.dataStore.edit { it[USERNAME_KEY] = username }
    }

    suspend fun getUserID(): Int? {
        return context.dataStore.data.firstOrNull()?.get(USERID_KEY)
    }

    suspend fun saveUserId(userId: Int) {
        context.dataStore.edit { it[USERID_KEY] = userId }
    }


}
