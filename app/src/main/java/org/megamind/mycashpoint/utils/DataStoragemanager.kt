package org.megamind.mycashpoint.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.firstOrNull
import org.megamind.mycashpoint.data.data_source.local.entity.UserEntity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class DataStorageManager(private val context: Context) {

    companion object {
        private val USERNAME_KEY = stringPreferencesKey("username")
        private val USERID_KEY = intPreferencesKey("userId")
        private val CODE_AGENCE = stringPreferencesKey("codeAgence")

        private val TOKEN_KEY = stringPreferencesKey("token")
    }


    suspend fun getToken(): String? {
        return context.dataStore.data.firstOrNull()?.get(TOKEN_KEY)
    }

    suspend fun saveToken(token: String) {
        context.dataStore.edit { it[TOKEN_KEY] = token }
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

    suspend fun saveCodeAgence(codeAgence: String) {

        context.dataStore.edit { it[CODE_AGENCE] = codeAgence }

    }

    suspend fun codeAgence(): String? {

        return context.dataStore.data.firstOrNull()?.get(CODE_AGENCE)
    }


}
