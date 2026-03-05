package org.megamind.mycashpoint.ui.screen.splash

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import org.megamind.mycashpoint.data.data_source.remote.dto.auth.Role
import org.megamind.mycashpoint.utils.DataStorageManager

import org.megamind.mycashpoint.utils.decodeJwtPayload

class SplashViewModel(
    private val dataStorageManager: DataStorageManager
) : ViewModel() {


    val TAG = "loginViewModel"
    private val _uiEvent =
        MutableSharedFlow<SplashUiEvent>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val uiEvent = _uiEvent.asSharedFlow()

    init {
        checkUserConnection()
    }

    private fun checkUserConnection() = viewModelScope.launch {
        val token = dataStorageManager.getToken()
        val url = dataStorageManager.getUrl()


//        if(url.isNullOrBlank()){
//            _uiEvent.emit(SplashUiEvent.NavigateToEtablissement)
//            Log.e(TAG, "url null")
//            return@launch
//        }
        // 1. Pas de token  login direct
        if (token.isNullOrBlank()) {
            _uiEvent.emit(SplashUiEvent.NavigateToLogin)
            Log.e(TAG, "token null")
            return@launch
        }

        // 2. Token expiré  login
        if (isTokenExpired(token)) {
            _uiEvent.emit(SplashUiEvent.NavigateToLogin)
            Log.e(TAG, "token expirer")
            return@launch
        }

        // 3. Extraire le role du token JWT
        val role = decodeJwtPayload(token).optString("role")

        when (role) {

            Role.ADMIN.name -> _uiEvent.emit(SplashUiEvent.NavigateToAdminHomeScreen)
            Role.AGENT.name -> _uiEvent.emit(SplashUiEvent.NavigateToAgentHomeScreen)
            else -> _uiEvent.emit(SplashUiEvent.NavigateToLogin)
        }


    }

    private fun isTokenExpired(token: String): Boolean {
        return try {
            val claims = decodeJwtPayload(token)
            val exp = claims.optLong("exp", 0)
            if (exp == 0L) return false

            val now = System.currentTimeMillis() / 1000
            now >= exp
        } catch (e: Exception) {
            true
        }
    }
}


data class SplashUiState(
    val isUserLoggedIn: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)


sealed class SplashUiEvent {
    object NavigateToLogin : SplashUiEvent()
    object NavigateToAgentHomeScreen : SplashUiEvent()
    object NavigateToAdminHomeScreen : SplashUiEvent()

//    object NavigateToEtablissement: SplashUiEvent()


}