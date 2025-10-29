package org.megamind.mycashpoint.ui.screen.transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.megamind.mycashpoint.data.data_source.local.entity.TransactionEntity
import org.megamind.mycashpoint.data.data_source.local.entity.TypTransct
import org.megamind.mycashpoint.domain.repository.TransactionRepository
import org.megamind.mycashpoint.utils.Constants
import org.megamind.mycashpoint.utils.Result

class TransationViewModel(private val repository: TransactionRepository) : ViewModel() {

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

    fun onNomClentChange(nom: String) {
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

    fun _onNoteChange(note: String) {
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

    fun onTypeSelected(type: TypTransct) {
        _uiState.update {
            it.copy(selectedType = type)
        }
    }


    fun onSaveClick(operateurId: Int) {

       val montant = _montant.toDouble()

        if (montant <=0) {
            _uiState.update {
                it.copy(isMontantError = true)
            }

            return
        }

        if (_nomClient.isEmpty()) {
            _uiState.update {
                it.copy(isNomError = true)
            }
            return
        }

        if (_telephClient.isEmpty()) {
            _uiState.update {
                it.copy(isTelephClientError = true)
            }
        }

        if (_nomBenef.isEmpty() && _typeTransact == TypTransct.DEPOT) {
            _uiState.update {
                it.copy(isNomBenefError = true)
            }
            return
        }

        if (_telephBenef.isEmpty()) {

            _uiState.update {
                it.copy(isTelephBenefError = true)
            }
            return
        }


        val transaction = TransactionEntity(
            idOperateur = operateurId,
            type = _typeTransact,
            device = _devise,
            montant = _montant.toDouble(),
            nomClient = _nomClient,
            numClient = _telephClient,
            nomBeneficaire = _nomBenef,
            numBeneficaire = _telephBenef,
            note = _note,
        )

        viewModelScope.launch {

            repository.ajouterTransactionEtMettreAJourSolde(transaction).collect { result ->

                when (result) {

                    Result.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }

                    is Result.Succes<*> -> {
                        _uiState.update { it.copy(isLoading = false) }
                        _uiEvent.emit(TransactionUiEvent.TransactionSaved)
                    }

                    is Result.Error<*> -> {
                        _uiState.update { it.copy(isLoading = false) }
                        _uiEvent.emit(
                            TransactionUiEvent.TransactionError(
                                result.e?.message ?: "Erreur inconnue"
                            )
                        )
                    }
                }

            }
        }
    }


    private fun validateInput() {



    }

}

data class TransactionUiState(

    val isLoading: Boolean = false,

    val selectedDevise: Constants.Devise = Constants.Devise.CDF,
    val selectedType: TypTransct = TypTransct.DEPOT,
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


    )


sealed class TransactionUiEvent() {


    object TransactionSaved : TransactionUiEvent()
    data class TransactionError(val errorMessage: String) : TransactionUiEvent()

}
