package org.megamind.mycashpoint.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class DataStorageManager(private val context: Context) {

    companion object {
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val LAST_SOLDE_SYNC_AT = longPreferencesKey("last_solde_sync_at")

        private val LAST_TRANSACTION_SYNC_AT = longPreferencesKey("last_transaction_sync_at")
    }

    suspend fun getToken(): String? {
        return context.dataStore.data.firstOrNull()?.get(TOKEN_KEY)
    }

    suspend fun saveToken(token: String) {
        context.dataStore.edit { it[TOKEN_KEY] = token }
    }

    suspend fun getLastSoldeSyncAt(): Long {
        return context.dataStore.data
            .map { prefs -> prefs[LAST_SOLDE_SYNC_AT] ?: 0L }  // valeur par défaut = 0
            .first()
    }

    suspend fun saveLastSoldeSyncAt(lastSyncAt: Long) {
        context.dataStore.edit { it[LAST_SOLDE_SYNC_AT] = lastSyncAt }
    }

    suspend fun getLastTransactionSyncAt(): Long {
        return context.dataStore.data.map { prefs ->
            prefs[LAST_TRANSACTION_SYNC_AT] ?: 0L
        }.first()

    }


    suspend fun saveLastTransactionSyncAt(lastSyncAt: Long) {
        context.dataStore.edit { it[LAST_TRANSACTION_SYNC_AT] = lastSyncAt }

    }


}
