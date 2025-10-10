package org.megamind.mycashpoint.ui.screen.auth

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
import org.megamind.mycashpoint.domain.repository.UserRepository
import org.megamind.mycashpoint.utils.Result
import org.megamind.mycashpoint.utils.UtilsFonctions

class RegisterViewModel(private val userRepository: UserRepository) : ViewModel() {


    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<RegisterUiEvent>()
    val uiEvent: SharedFlow<RegisterUiEvent> = _uiEvent.asSharedFlow()


    fun onEmailChange(email: String) {
        _uiState.update {
            it.copy(
                email = email,
                isEmailError = !UtilsFonctions.isValidEmail(email)
            )
        }
    }

    fun onPasswordChange(password: String) {
        _uiState.update {
            it.copy(password = password, isPasswordError = _uiState.value.password.length <= 8)
        }
    }

    fun onPasswordRepeatChange(password: String) {
        _uiState.update {
            it.copy(
                passWordRepeat = password,
                isPasswordRepError = _uiState.value.password != _uiState.value.passWordRepeat
            )
        }
    }

    fun onNameChange(name: String) {
        _uiState.update {
            it.copy(userName = name, isNameError = _uiState.value.userName.isEmpty())
        }
    }


    fun onRegister() {


        if (_uiState.value.userName.isEmpty()) {

            _uiState.update {
                it.copy(isNameError = true)
            }
            return
        }
        if (_uiState.value.email.isEmpty()) {

            _uiState.update {
                it.copy(isEmailError = true)
            }
            return
        }

        if (_uiState.value.password.isEmpty()) {

            _uiState.update {
                it.copy(isPasswordError = true)
            }
            return
        }


        if (_uiState.value.password != _uiState.value.passWordRepeat) {

            _uiState.update {
                it.copy(isPasswordRepError = true)
            }
            return
        }



        viewModelScope.launch(Dispatchers.IO) {

            userRepository
                .register(
                    name = _uiState.value.userName,
                    email = _uiState.value.email,
                    password = _uiState.value.password,
                ).collect { result ->

                    when (val result = result) {

                        is Result.Loading -> {
                            _uiState.update {
                                it.copy(isLoading = true)
                            }
                        }

                        is Result.Succes -> {
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
    }


}


data class RegisterUiState(
    val userName: String = "",
    val email: String = "",
    val password: String = "",
    val passWordRepeat: String = "",
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val isPasswordShown: Boolean = false,
    val isEmailError: Boolean = false,
    val isPasswordError: Boolean = false,
    val isPasswordRepError: Boolean = false,
    val isNameError: Boolean = false,
    val isRegisting: Boolean = false,
) {

}


sealed class RegisterUiEvent {

    object NavigateToHome : RegisterUiEvent()
    data class ShowError(val message: String) : RegisterUiEvent()


}

