package org.megamind.mycashpoint.ui.screen.auth.edit_user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.megamind.mycashpoint.data.data_source.remote.dto.auth.EditUserRequeste
import org.megamind.mycashpoint.domain.usecase.auth.EditUserNameOrPasswordUseCase
import org.megamind.mycashpoint.utils.DataStorageManager
import org.megamind.mycashpoint.utils.Result
import org.megamind.mycashpoint.utils.decodeJwtPayload

class EditUserViewModel(
    private val editUserNameOrPasswordUseCase: EditUserNameOrPasswordUseCase,
    private val dataStorageManager: DataStorageManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditUserUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<EditUserUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    init {
        viewModelScope.launch {
            loadCurrentUser()
        }
    }

  suspend fun loadCurrentUser() {
        val token = dataStorageManager.getToken()
        if (token != null) {
            val claims = decodeJwtPayload(token)
            val name = claims.optString("name")
            _uiState.update { it.copy(username = name) }
        }
    }

    fun onUsernameChange(value: String) {
        _uiState.update { it.copy(username = value) }
    }

    fun onCurrentPasswordChange(value: String) {
        _uiState.update { it.copy(currentPassword = value) }
    }

    fun onNewPasswordChange(value: String) {
        _uiState.update { it.copy(newPassword = value) }
    }

    fun onConfirmNewPasswordChange(value: String) {
        _uiState.update { it.copy(confirmNewPassword = value) }
    }

    fun onSave() {
        val currentState = _uiState.value

        if (currentState.currentPassword.isBlank()) {
            emitError("Le mot de passe actuel est requis")
            return
        }

        if (currentState.newPassword.isNotBlank() && currentState.newPassword != currentState.confirmNewPassword) {
            emitError("Les nouveaux mots de passe ne correspondent pas")
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val token = dataStorageManager.getToken()
            if (token == null) {
                emitError("Erreur d'authentification")
                _uiState.update { it.copy(isLoading = false) }
                return@launch
            }

            val request = EditUserRequeste(
                currentPassword = currentState.currentPassword,
                newPassword = currentState.newPassword,
                newUsername = currentState.username
            )

            editUserNameOrPasswordUseCase(token, request).collect { result ->
                when (result) {
                    is Result.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                    is Result.Success -> {
                        _uiState.update { it.copy(isLoading = false) }
                        dataStorageManager.saveToken("")
                        _uiEvent.emit(EditUserUiEvent.NavigateToLogin)
                    }
                    is Result.Error -> {
                        _uiState.update { it.copy(isLoading = false) }
                        emitError(result.e?.message ?: "Une erreur est survenue")
                    }
                }
            }
        }
    }

    private fun emitError(message: String) {
        viewModelScope.launch {
            _uiEvent.emit(EditUserUiEvent.Error(message))
        }
    }
}

data class EditUserUiState(
    val username: String = "",
    val currentPassword: String = "",
    val newPassword: String = "",
    val confirmNewPassword: String = "",
    val isLoading: Boolean = false
)

sealed class EditUserUiEvent {
    data class Error(val message: String) : EditUserUiEvent()
    data class Success(val message: String) : EditUserUiEvent()
    object NavigateToLogin : EditUserUiEvent()
}
