package org.megamind.mycashpoint.ui.screen.splash

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import org.megamind.mycashpoint.domain.usecase.auth.GetUserByIdUseCase
import org.megamind.mycashpoint.ui.screen.main.utils.DataStorageManager

import org.megamind.mycashpoint.ui.screen.main.utils.Result
import org.megamind.mycashpoint.ui.screen.main.utils.decodeJwtPayload
class SplashViewModel(
    private val getUserByIdUseCase: GetUserByIdUseCase, // Retourne le user dans Room
    private val dataStorageManager: DataStorageManager
) : ViewModel() {


    val TAG = "loginViewModel"
    private val _uiEvent = MutableSharedFlow<SplashUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    init {
        checkUserConnection()
    }

    private fun checkUserConnection() = viewModelScope.launch {
        val token = dataStorageManager.getToken()

        // 1. Pas de token  login direct
        if (token.isNullOrBlank()) {
            _uiEvent.emit(SplashUiEvent.NavigateToLogin)
            Log.e(TAG,"token null")
            return@launch
        }

        // 2. Token expiré  login
        if (isTokenExpired(token)) {
            _uiEvent.emit(SplashUiEvent.NavigateToLogin)
            Log.e(TAG,"token expirer")
            return@launch
        }

        // 3. Extraire les infos du token JWT
        val claims = decodeJwtPayload(token)
        val userId = claims.optString("sub").toLongOrNull()

        if (userId == null) {
            _uiEvent.emit(SplashUiEvent.NavigateToLogin)
            Log.e(TAG,"user  id du token est null")
            return@launch
        }

        // 4. Charger le user depuis ROOM
        getUserByIdUseCase(userId).collect { result ->

            when (result) {

                is Result.Loading -> Unit

                is Result.Error -> {
                    // Si Room renvoie une erreur → login
                    _uiEvent.emit(SplashUiEvent.NavigateToLogin)
                    Log.e(TAG,"Error de fetch ${result.e?.message}")
                }

                is Result.Success -> {
                    val localUser = result.data

                    // 5. Aucun user en local → login
                    if (localUser == null) {
                        _uiEvent.emit(SplashUiEvent.NavigateToLogin)
                        Log.e(TAG,"Local user est null")
                        return@collect
                    }

                    // 6. Comparaison DB <-> Token
                    val tokenUserName = claims.optString("name")

                    if (localUser.name != tokenUserName) {
                        _uiEvent.emit(SplashUiEvent.NavigateToLogin)
                        Log.e(TAG,"Local user est different de celui du token")
                        return@collect
                    }

                    // 7. Tout est OK !
                    _uiEvent.emit(SplashUiEvent.NavigateToHome)
                }
            }
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
    object NavigateToHome : SplashUiEvent()
}