package org.megamind.mycashpoint.ui.screen.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.megamind.mycashpoint.data.data_source.remote.dto.auth.Role
import org.megamind.mycashpoint.utils.DataStorageManager
import org.megamind.mycashpoint.utils.decodeJwtPayload

class MainViewModel(dataStorageManager: DataStorageManager) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState

    init {
        viewModelScope.launch {
            val token = dataStorageManager.getToken()

            if (token != null) {
                try {
                    val payload = decodeJwtPayload(token)
                    val userRoleString = payload.getString("role")
                    val role = Role.valueOf(userRoleString)
                    
                    _uiState.update {
                        it.copy(userRole = role)
                    }
                    
                    Log.d("MainViewModel", "User role loaded: $role")
                } catch (e: Exception) {
                    Log.e("MainViewModel", "Error decoding JWT or parsing role", e)
                    // Garder le rôle par défaut (AGENT)
                }
            } else {
                Log.d("MainViewModel", "No token found, using default role: AGENT")
            }
        }
    }
}

data class MainUiState(
    val userRole: Role = Role.AGENT
)