package org.megamind.mycashpoint.ui.screen.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.megamind.mycashpoint.data.data_source.remote.dto.auth.Role
import org.megamind.mycashpoint.domain.model.Agence
import org.megamind.mycashpoint.domain.usecase.auth.RegisterUseCase
import org.megamind.mycashpoint.utils.Result
import org.megamind.mycashpoint.utils.UtilsFonctions

class RegisterViewModel(private val registerUseCase: RegisterUseCase) : ViewModel() {

    val TAG = "Register ViewModel"
    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<RegisterUiEvent>()
    val uiEvent: SharedFlow<RegisterUiEvent> = _uiEvent.asSharedFlow()


    fun onPasswordChange(password: String) {
        _uiState.update {
            it.copy(password = password, isPasswordError = false)
        }
    }

    fun onPasswordRepeatChange(password: String) {
        _uiState.update {
            it.copy(
                passWordRepeat = password,
                isPasswordRepError = false
            )
        }
    }

    fun onNameChange(name: String) {
        _uiState.update {
            it.copy(userName = name, isNameError = false)
        }
    }

    fun onAgenceChange(agence: Agence) {
        _uiState.update {
            it.copy(agence = agence.designation, selectedAgence = agence)
        }
    }

    fun onRegister() {

        if (!validateForm()) return

        val userName = _uiState.value.userName
        val password = _uiState.value.password
        val agenceId = _uiState.value.selectedAgence?.codeAgence ?: return
        val role = _uiState.value.selecteRole.name

        viewModelScope.launch(Dispatchers.IO) {

            registerUseCase(
                userName = userName,
                password = password,
                agenceId = agenceId,
                role = role

            ).collect { result ->

                when (val result = result) {

                    is Result.Loading -> {
                        _uiState.update {
                            it.copy(isLoading = true)
                        }
                    }

                    is Result.Success -> {
                        _uiState.update {
                            it.copy(isLoading = false)
                        }
                        _uiEvent.emit(RegisterUiEvent.NavigateToHome)

                    }

                    is Result.Error -> {
                        _uiState.update {
                            it.copy(isLoading = false)
                        }
                        _uiEvent.emit(
                            RegisterUiEvent.ShowError(
                                result.e?.message ?: "Unknown error"
                            )
                        )

                    }

                }


            }

        }


    }

    fun onPasswordVisibilityChange() {
        _uiState.update {
            it.copy(isPasswordVisible = !_uiState.value.isPasswordVisible)
        }


        Log.i(TAG, "isPasswordVisible: ${_uiState.value.isPasswordVisible}")
    }

    fun onAgenceMenuDismiss() {
        _uiState.update {
            it.copy(isAgenceExpanded = false)
        }
    }

    fun onAgenceMenuExpanded() {
        _uiState.update {
            it.copy(isAgenceExpanded = true)
        }
    }

    fun onUserRoleChange(role: Role) {
        _uiState.update {
            it.copy(selecteRole = role)
        }
    }

    private fun validateForm(): Boolean {
        var isValid = true

        if (_uiState.value.userName.isEmpty()) {
            _uiState.update {
                it.copy(isNameError = true)
            }
            isValid = false
        }

        if (_uiState.value.password.isEmpty() || _uiState.value.password.length <= 8) {
            _uiState.update {
                it.copy(isPasswordError = true)
            }
            isValid = false

        }

        if (_uiState.value.passWordRepeat.isEmpty() || _uiState.value.passWordRepeat != _uiState.value.password) {
            _uiState.update {
                it.copy(isPasswordRepError = true)
            }
            isValid = false
        }

        if (_uiState.value.selecteRole == Role.AGENT && _uiState.value.selectedAgence == null) {
            _uiState.update {
                it.copy(isAgenceExpanded = true)
            }
            isValid = false
        }

        return isValid
    }

}


data class RegisterUiState(
    val userName: String = "",
    val password: String = "",
    val agence: String = "",
    val passWordRepeat: String = "",
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val isEmailError: Boolean = false,
    val isPasswordError: Boolean = false,
    val isPasswordRepError: Boolean = false,
    val isNameError: Boolean = false,
    val isRegisting: Boolean = false,
    val selectedAgence: Agence? = null,
    val isAgenceExpanded: Boolean = false,
    val selecteRole: Role = Role.AGENT
) {


}


sealed class RegisterUiEvent {

    object NavigateToHome : RegisterUiEvent()
    data class ShowError(val message: String) : RegisterUiEvent()


}

