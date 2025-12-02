package org.megamind.mycashpoint.ui.screen.operateur

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.megamind.mycashpoint.domain.model.Operateur
import org.megamind.mycashpoint.utils.DataStorageManager

class OperateurViewModel(private val datastorageManager: DataStorageManager) : ViewModel() {


    private val _uiSate = MutableStateFlow(OperateurUiState())
    val uiState = _uiSate.asStateFlow()

    private val _uiEvent = MutableSharedFlow<OperateurUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    fun onOperateurSelected(operateur: Operateur) {
        _uiSate.update {
            it.copy(selectedOperateur = operateur)
        }

    }

    fun onLogOut() {
        viewModelScope.launch {
            datastorageManager.saveToken("")
            _uiEvent.emit(OperateurUiEvent.NavigateToLogin)
        }
    }

    fun onConfirmLogOutDialogShown() {
        _uiSate.update {
            it.copy(isConfirmLogOutDialogShown = true)
        }

    }

    fun onConfirmLogOutDialogDismiss() {
        _uiSate.update {
            it.copy(isConfirmLogOutDialogShown = false)
        }
    }


}

data class OperateurUiState(
    val selectedOperateur: Operateur? = null,
    val isConfirmLogOutDialogShown: Boolean = false,

    )

sealed class OperateurUiEvent {
    object NavigateToLogin : OperateurUiEvent()
}


