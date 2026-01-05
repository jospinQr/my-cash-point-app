package org.megamind.mycashpoint.ui.screen.operateur

import android.util.Log
import androidx.compose.ui.util.fastMaxOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.megamind.mycashpoint.domain.model.Operateur
import org.megamind.mycashpoint.domain.model.Solde
import org.megamind.mycashpoint.domain.model.Transaction
import org.megamind.mycashpoint.domain.usecase.etablissement.GetEtablissementFromServerAndInsertLocalyUseCase
import org.megamind.mycashpoint.domain.usecase.solde.GetRemoteSoldesByAgenceAndUserUseCase
import org.megamind.mycashpoint.domain.usecase.solde.GetSoldeForSyncUsecas
import org.megamind.mycashpoint.domain.usecase.solde.InsertSoldeListLocallyUseCase
import org.megamind.mycashpoint.domain.usecase.transaction.GetRemoteTransactionsByAgenceAndUserUseCase
import org.megamind.mycashpoint.domain.usecase.transaction.GetTransactionForSynUseCase
import org.megamind.mycashpoint.domain.usecase.transaction.InsertTransactionListLocallyUseCase
import org.megamind.mycashpoint.utils.DataStorageManager
import org.megamind.mycashpoint.utils.Result
import org.megamind.mycashpoint.utils.decodeJwtPayload

class OperateurViewModel(
    private val datastorageManager: DataStorageManager,
    private val getRemoteTransactionsByAgenceAndUserUseCase: GetRemoteTransactionsByAgenceAndUserUseCase,
    private val getSoldeForSyncUsecas: GetSoldeForSyncUsecas,
    private val getTransactionForSyncUseCase: GetTransactionForSynUseCase,
    private val insertTransactionListLocalyUseCase: InsertTransactionListLocallyUseCase,
    private val getAndInsertEtablissementUseCase: GetEtablissementFromServerAndInsertLocalyUseCase
) : ViewModel() {


    val TAG = "OperateurViewModel"

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


    private fun insertTransactionsLocaly(transactions: List<Transaction>) {
        viewModelScope.launch {
            insertTransactionListLocalyUseCase(transactions).collect { result ->
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
                        Log.e(TAG, "Error ${result.e?.message}")
                        _uiEvent.emit(
                            OperateurUiEvent.ShowError(
                                result.e?.message ?: "Erreur inconnue"
                            )
                        )
                    }

                }
            }
        }

    }


    fun getAllSoldeFromServerAndInsertInLocaldb() {
        viewModelScope.launch {
            val lastSyncAt = datastorageManager.getLastSoldeSyncAt()
            val agenceCode = datastorageManager.getToken()?.let { decodeJwtPayload(it) }!!
                .optString("agence_code")
            getSoldeForSyncUsecas(
                lastSyncAt = lastSyncAt,
                agenceCode = agenceCode
            ).collect { result ->

                when (val result = result) {
                    is Result.Loading -> {
                        _uiSate.update {
                            it.copy(isGetAllSoldeLoading = true)
                        }

                    }

                    is Result.Success -> {
                        _uiSate.update {
                            it.copy(isGetAllSoldeLoading = false)
                        }
                        getAllTransactFromServerAndInsertInLocaldb()


                    }

                    is Result.Error<*> -> {
                        _uiSate.update {
                            it.copy(
                                getAllSoldeError = result.e?.message,
                                isGetAllSoldeLoading = false
                            )
                        }
                        _uiEvent.emit(
                            OperateurUiEvent.ShowError(
                                result.e?.message
                                    ?: "Erreur inconnue lors téléchargement des données"
                            )
                        )
                        Log.e(TAG, "Error ${result.e?.message}")


                    }


                }


            }

        }

    }

    fun getAllTransactFromServerAndInsertInLocaldb() {
        viewModelScope.launch {
            val lastSyncAt = datastorageManager.getLastTransactionSyncAt()
            val agenceCode = datastorageManager.getToken()?.let { decodeJwtPayload(it) }!!
                .optString("agence_code")


            val token = datastorageManager.getToken()
            val userId = decodeJwtPayload(token!!).optString("sub")
            getTransactionForSyncUseCase(
                lastSyncAt = lastSyncAt,
                agenceCode = agenceCode,
                userId = userId.toLong()
            ).collect { result ->

                when (val result = result) {
                    is Result.Loading -> {
                        _uiSate.update {
                            it.copy(isGetAllTransactLoading = true)
                        }

                    }

                    is Result.Success -> {
                        _uiSate.update {
                            it.copy(isGetAllTransactLoading = false)
                        }

                    }

                    is Result.Error<*> -> {
                        _uiSate.update {
                            it.copy(
                                getAllTransactError = result.e?.message,
                                isGetAllTransactLoading = false
                            )
                        }
                        _uiEvent.emit(
                            OperateurUiEvent.ShowError(
                                result.e?.message
                                    ?: "Erreur inconnue lors téléchargement des données"
                            )
                        )
                        Log.e(TAG, "Error ${result.e?.message}")


                    }


                }


            }

        }

    }


    fun onMainMenuExpanded() {
        _uiSate.update {
            it.copy(isMainMenuExpanded = true)
        }
    }

    fun onMainMenuHidden() {
        _uiSate.update {
            it.copy(isMainMenuExpanded = false)
        }
    }

    fun onConfirmDownLoadDialogShown() {
        _uiSate.update {
            it.copy(isConfirmDownloadDialogShown = true)
        }
    }

    fun onConfirmDownLoadDialogHidden() {
        _uiSate.update {
            it.copy(isConfirmDownloadDialogShown = false)

        }
    }

    fun getEtablissement() {

        viewModelScope.launch {
            getAndInsertEtablissementUseCase().collect { result ->
                when (result) {
                    is Result.Loading -> {
                        _uiSate.update {
                            it.copy(isGetAllSoldeLoading = true)
                        }
                    }

                    is Result.Error<*> -> {
                        _uiSate.update {
                            it.copy(
                                getAllSoldeError = result.e?.message,
                                isGetAllSoldeLoading = false
                            )
                        }

                    }

                    is Result.Success -> {
                        _uiSate.update {
                            it.copy(isGetAllSoldeLoading = false)
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
    val insertAllTransctError: String? = null,

    val isInsertAllSoldeLoading: Boolean = false,
    val insertAllSoldeError: String? = null,

    val isGetAllSoldeLoading: Boolean = false,
    val getAllSoldeError: String? = null,

    val isGetAllTransactLoading: Boolean = false,
    val getAllTransactError: String? = null,


    val isMainMenuExpanded: Boolean = false,
    val isConfirmDownloadDialogShown: Boolean = false,

    ) {

}

sealed class OperateurUiEvent {
    object NavigateToLogin : OperateurUiEvent()
    class ShowError(val message: String) : OperateurUiEvent()

}


