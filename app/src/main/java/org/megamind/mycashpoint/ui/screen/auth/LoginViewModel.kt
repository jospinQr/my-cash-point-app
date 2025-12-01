package org.megamind.mycashpoint.ui.screen.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.megamind.mycashpoint.data.data_source.remote.dto.auth.Role
import org.megamind.mycashpoint.domain.model.Agence
import org.megamind.mycashpoint.domain.usecase.auth.LoginUseCase
import org.megamind.mycashpoint.utils.Result
import org.megamind.mycashpoint.utils.UtilsFonctions
import org.megamind.mycashpoint.utils.decodeJwtPayload

class LoginViewModel(
    private val loginUseCase: LoginUseCase,
) : ViewModel() {


    private val _uiState = MutableStateFlow(SignInUiState())
    val uiState: StateFlow<SignInUiState> = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<SignInUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()


    private val _email: String
        get() = _uiState.value.email

    private val _password: String
        get() = _uiState.value.password


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
            it.copy(password = password, isPasswordError = password.length <= 8)
        }
    }

    fun onAgenceChange(agence: Agence) {
        _uiState.update {
            it.copy(agence = agence.designation, selectedAgence = agence)
        }
    }

    fun onSignIn() {

        if (_email.isEmpty()) {
            _uiState.update {
                it.copy(isEmailError = true)
            }
            return
        }

        if (_password.isEmpty()) {
            _uiState.update {
                it.copy(isPasswordError = true)
            }
            return
        }


        viewModelScope.launch(Dispatchers.IO) {

            loginUseCase(
                _email,
                _password,

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

                        val userRole = decodeJwtPayload(result.data?.token!!).optString("role")
                        when (userRole) {
                            Role.ADMIN.name -> _uiEvent.emit(SignInUiEvent.NavigateToAdmintHomeScreen)
                            Role.AGENT.name -> {
                                _uiEvent.emit(SignInUiEvent.NavigateToAgentHomeScreen)

                            }

                            else -> {
                                _uiEvent.emit(SignInUiEvent.ShowError("Role Iconnu"))

                            }
                        }


                    }

                    is Result.Error -> {
                        _uiState.update {
                            it.copy(isLoading = false)
                        }
                        _uiEvent.emit(
                            SignInUiEvent.ShowError(
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
            it.copy(isPasswordShown = !it.isPasswordShown)
        }
    }

    fun sendPasswordResetEmail() {

        _uiState.update {
            it.copy(isSendingPasswordResetDialogShown = true)
        }
    }

    fun showPasswordResetDialog() {
        _uiState.update {
            it.copy(isSendingPasswordResetDialogShown = true)
        }
    }

    fun dismissPasswordResetDialog() {
        _uiState.update {
            it.copy(isSendingPasswordResetDialogShown = false)
        }
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
}


data class SignInUiState(
    val email: String = "",
    val password: String = "",
    val agence: String = "",
    val isLoading: Boolean = false,
    val isSendingPasswordResetDialogShown: Boolean = false,
    val isPasswordError: Boolean = false,
    val isSendingPasswordResetEmail: Boolean = false,
    val isSigningIn: Boolean = false,
    val isPasswordShown: Boolean = false,
    val isEmailError: Boolean = false,
    val selectedAgence: Agence? = null,
    val isAgenceExpanded: Boolean = false,

    ) {

}

sealed class SignInUiEvent {

    object NavigateToAgentHomeScreen : SignInUiEvent()
    object NavigateToAdmintHomeScreen : SignInUiEvent()

    data class ShowError(val message: String) : SignInUiEvent()


}




