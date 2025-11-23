package org.megamind.mycashpoint.ui.screen.transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.megamind.mycashpoint.domain.model.Transaction
import org.megamind.mycashpoint.domain.model.TransactionType
import org.megamind.mycashpoint.domain.usecase.transaction.InsertTransactionAndUpdateSoldesUseCase
import org.megamind.mycashpoint.domain.usecase.transaction.TransactionField
import org.megamind.mycashpoint.domain.usecase.transaction.TransactionValidationException
import org.megamind.mycashpoint.ui.screen.main.utils.Constants
import org.megamind.mycashpoint.ui.screen.main.utils.DataStorageManager
import org.megamind.mycashpoint.ui.screen.main.utils.Result
import org.megamind.mycashpoint.ui.screen.main.utils.decodeJwtPayload
import java.math.BigDecimal

class TransactionViewModel(
    private val insertTransactionAndUpdateSoldes: InsertTransactionAndUpdateSoldesUseCase,

    private val storageManager: DataStorageManager,

    ) : ViewModel() {

    private val _uiState = MutableStateFlow(TransactionUiState())
    val uiSate = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<TransactionUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()
    private val _montant get() = _uiState.value.montant
    private val _nomClient get() = _uiState.value.nomClient
    private val _telephClient get() = _uiState.value.telephClient
    private val _nomBenef get() = _uiState.value.nomBenef
    private val _telephBenef get() = _uiState.value.telephBenef
    private val _note get() = _uiState.value.note
    private val _devise get() = _uiState.value.selectedDevise
    private val _typeTransact get() = _uiState.value.selectedType


    fun onMontantChange(montant: String) {
        _uiState.update {
            it.copy(montant = montant, isMontantError = false)
        }
    }

    fun onNomClientChange(nom: String) {
        _uiState.update {
            it.copy(nomClient = nom, isNomError = false)
        }
    }

    fun onTelephClientChange(teleph: String) {
        _uiState.update {
            it.copy(telephClient = teleph, isTelephClientError = false)
        }
    }

    fun onNomBenefChange(nom: String) {
        _uiState.update {
            it.copy(nomBenef = nom, isNomBenefError = false)
        }

    }

    fun onTelephBenefChange(teleph: String) {
        _uiState.update {
            it.copy(telephBenef = teleph, isTelephBenefError = false)
        }
    }

    fun onNoteChange(note: String) {
        _uiState.update {
            it.copy(note = note)
        }
    }


    fun onDeviseSelected(devise: Constants.Devise) {
        _uiState.update {
            it.copy(selectedDevise = devise)
        }
    }


    fun onFormVisble() {

        _uiState.update {
            it.copy(isFormVisble = true)
        }
    }

    fun onFormInvisble() {
        _uiState.update {
            it.copy(isFormVisble = false)
        }
    }

    fun onTypeSelected(type: TransactionType) {
        _uiState.update {
            it.copy(selectedType = type)
        }
    }


    fun onSaveClick(operateurId: Int) {


        viewModelScope.launch {
            val claims = decodeJwtPayload(storageManager.getToken()!!)


            val transaction = Transaction(
                idOperateur = operateurId,
                type = _typeTransact,
                device = _devise,
                montant = _montant.toBigDecimalOrNull() ?: BigDecimal(0),
                nomClient = _nomClient,
                numClient = _telephClient,
                nomBeneficaire = _nomBenef,
                numBeneficaire = _telephBenef,
                note = _note,
                creePar = claims.optString("sub").toLong(),
                codeAgence = claims.optString("agence_code")
            )

            insertTransactionAndUpdateSoldes(transaction).collect { result ->

                when (result) {

                    Result.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }

                    is Result.Success<*> -> {

                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                montant = "",
                                nomClient = "",
                                telephClient = "",
                                nomBenef = "",
                                telephBenef = "",
                                isFormVisble = false
                            )
                        }
                        _uiEvent.emit(TransactionUiEvent.ShowSuccessMessage("Transaction enregistrée"))
                        _uiEvent.emit(
                            TransactionUiEvent.ReprintReceipt(
                                result.data!!,
                                claims.optString("name")
                            )
                        )
                    }

                    is Result.Error<*> -> {
                        _uiState.update { it.copy(isLoading = false, isFormVisble = false) }
                        when (val ex = result.e) {
                            is TransactionValidationException.FieldRequired -> {
                                _uiState.update {
                                    when (ex.field) {
                                        TransactionField.OPERATEUR -> it
                                        TransactionField.TYPE -> it
                                        TransactionField.DEVISE -> it
                                        TransactionField.NOM_CLIENT -> it.copy(isNomError = true)
                                        TransactionField.TEL_CLIENT -> it.copy(isTelephClientError = true)
                                        TransactionField.NOM_BENEF -> it.copy(isNomBenefError = true)
                                        TransactionField.TEL_BENEF -> it.copy(isTelephBenefError = true)
                                    }
                                }
                            }

                            is TransactionValidationException.InvalidAmount -> {
                                _uiState.update { it.copy(isMontantError = true) }
                            }

                            else -> {
                                _uiEvent.emit(
                                    TransactionUiEvent.ShowErrorMessage(
                                        ex?.message ?: "Erreur inconnue"
                                    )
                                )
                            }
                        }
                    }
                }

            }
        }
    }


    fun onConfirmDialogShown() {

        _uiState.update {
            it.copy(isConfirmDialogShown = true)
        }

    }

    fun onConfirmDialogDismiss() {
        _uiState.update {
            it.copy(isConfirmDialogShown = false)
        }
    }


}

data class TransactionUiState(

    val isLoading: Boolean = false,

    val selectedDevise: Constants.Devise = Constants.Devise.USD,
    val selectedType: TransactionType = TransactionType.DEPOT,
    val isFormVisble: Boolean = false,

    val montant: String = "",
    val nomClient: String = "",
    val telephClient: String = "",

    val nomBenef: String = "",
    val telephBenef: String = "",
    val note: String = "",

    val isMontantError: Boolean = false,
    val isNomError: Boolean = false,
    val isTelephClientError: Boolean = false,
    val isNomBenefError: Boolean = false,
    val isTelephBenefError: Boolean = false,
    val solde: BigDecimal = BigDecimal(0),
    val isConfirmDialogShown: Boolean = false


)


sealed class TransactionUiEvent() {


    data class ShowSuccessMessage(val successMessage: String) : TransactionUiEvent()
    data class ShowErrorMessage(val errorMessage: String) : TransactionUiEvent()

    data class ReprintReceipt(val transaction: Transaction, val user: String) :
        TransactionUiEvent()

}
