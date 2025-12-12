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
import org.megamind.mycashpoint.domain.model.Transaction
import org.megamind.mycashpoint.domain.usecase.transaction.InsertAllTransactUserCase
import org.megamind.mycashpoint.utils.DataStorageManager
import org.megamind.mycashpoint.utils.Result

class OperateurViewModel(
    private val datastorageManager: DataStorageManager,
    private val insertAllTransactUserCase: InsertAllTransactUserCase,

) : ViewModel() {


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


    fun insertAllTransaction(transactions: List<Transaction>) {
        viewModelScope.launch {
            insertAllTransactUserCase(transactions).collect { result ->
                when (result) {
                    is Result.Loading -> {
                        _uiSate.update {
                            it.copy(isInsertAllTransactLoading = true)
                        }
                    }

                    is Result.Success -> {
                        _uiSate.update {
                            it.copy(isInsertAllTransactLoading = false)
                        }
                    }

                    is Result.Error<*> -> {
                        _uiSate.update {
                            it.copy(
                                isInsertAllTransactLoading = false,
                                insertAllTransctError = result.e?.message
                            )
                        }
                    }

                }
            }
        }

    }

}

data class OperateurUiState(
    val selectedOperateur: Operateur? = null,
    val isConfirmLogOutDialogShown: Boolean = false,
    val isInsertAllTransactLoading: Boolean = false,
    val insertAllTransctError: String? = null

)

sealed class OperateurUiEvent {
    object NavigateToLogin : OperateurUiEvent()
}


