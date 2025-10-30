package org.megamind.mycashpoint.ui.screen.caisse

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.megamind.mycashpoint.data.data_source.local.entity.SoldeEntity
import org.megamind.mycashpoint.domain.model.Operateur
import org.megamind.mycashpoint.domain.model.operateurs
import org.megamind.mycashpoint.domain.repository.SoldeRepository
import org.megamind.mycashpoint.utils.Constants
import org.megamind.mycashpoint.utils.DataStorageManager
import org.megamind.mycashpoint.utils.Result

class CaisseViewModel(
    private val repository: SoldeRepository,
    val storageManager: DataStorageManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(CaisseUiState())
    val uiState = _uiState.asStateFlow()

    private val _soldes = MutableStateFlow<List<SoldeEntity>>(emptyList())
    val soldes = _soldes.asStateFlow()

    private val _uiEvent = MutableSharedFlow<CaisseUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    fun onSoldeChange(solde: String) {
        _uiState.update {
            it.copy(solde = solde)
        }

    }


    init {
        getSoldes()
    }

    fun onOperateurChange(operateur: Operateur) {
        _uiState.update {
            it.copy(selectedoperateur = operateur)
        }
    }

    fun onDeviseChange(devise: Constants.Devise) {
        _uiState.update {
            it.copy(selectedDevise = devise)
        }

    }

    fun onSeuilChange(seuil: String) {
        _uiState.update {
            it.copy(seuilAlert = seuil)
        }
    }


    fun getSoldes() {
        viewModelScope.launch {
            repository.getAll().collect { result ->

                when (result) {

                    is Result.Loading -> {
                        _uiState.update {
                            it.copy(isLoading = true)
                        }
                    }

                    is Result.Succes<*> -> {
                        _uiState.update {
                            it.copy(isLoading = false)
                        }
                        _soldes.value = result.data ?: emptyList()

                    }

                    is Result.Error<*> -> {
                        _uiState.update {
                            it.copy(isLoading = false, errorMessage = result.e?.message)
                        }

                    }
                }

            }

        }

    }

    fun onSaveClick() {


        viewModelScope.launch {
            val solde = SoldeEntity(
                idOperateur = uiState.value.selectedoperateur.id,
                montant = uiState.value.solde.toDouble(),
                devise = uiState.value.selectedDevise,
                seuilAlerte = uiState.value.seuilAlert?.toDouble(),
                dernierMiseAJour = System.currentTimeMillis(),
                misAJourPar = storageManager.getUserID()!!

            )
            repository.insertOrUpdate(solde).collect { result ->

                when (result) {

                    Result.Loading -> {
                        _uiState.update {
                            it.copy(isLoading = true)
                        }
                    }

                    is Result.Succes -> {
                        _uiState.update {
                            it.copy(isLoading = false)
                        }
                        _uiEvent.emit(CaisseUiEvent.CaisseSaved)
                        getSoldes()
                    }

                    is Result.Error<*> -> {
                        _uiState.update {
                            it.copy(isLoading = false)
                        }
                        _uiEvent.emit(
                            CaisseUiEvent.CaisseError(
                                result.e?.message ?: "Erreur inconnue"
                            )
                        )
                    }


                }
            }
        }

    }

    fun onBottomSheetShown() {
        _uiState.update {
            it.copy(isBottomSheetShown = true)
        }
    }

    fun onBottomSheetHide() {
        _uiState.update {
            it.copy(isBottomSheetShown = false)
        }
    }

    fun onOperateurMenuExpanded() {
        _uiState.update {
            it.copy(isOperateurExpanded = true)
        }
    }

    fun onOperateurMenuDismiss() {
        _uiState.update {
            it.copy(isOperateurExpanded = false)
        }
    }

}


data class CaisseUiState(

    val solde: String = "",
    val selectedoperateur: Operateur = operateurs.first(),
    val selectedDevise: Constants.Devise = Constants.Devise.USD,
    val seuilAlert: String? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isBottomSheetShown: Boolean = false,
    val isOperateurExpanded: Boolean = false
) {

}

sealed class CaisseUiEvent {

    object CaisseSaved : CaisseUiEvent()
    data class CaisseError(val errorMessage: String) : CaisseUiEvent()

}


