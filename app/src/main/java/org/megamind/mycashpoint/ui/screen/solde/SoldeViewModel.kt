package org.megamind.mycashpoint.ui.screen.solde

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
import org.megamind.mycashpoint.domain.model.SoldeType
import org.megamind.mycashpoint.domain.model.operateurs
import org.megamind.mycashpoint.domain.usecase.solde.GetSoldeByOperateurEtTypeEtDeviseUseCase
import org.megamind.mycashpoint.domain.usecase.solde.SaveOrUpdateSoldeUseCase
import org.megamind.mycashpoint.domain.usecase.solde.SoldeValidationException
import org.megamind.mycashpoint.utils.Constants
import org.megamind.mycashpoint.utils.DataStorageManager
import org.megamind.mycashpoint.utils.Result
import org.megamind.mycashpoint.utils.decodeJwtPayload
import org.megamind.mycashpoint.utils.toBigDecimalOrNull
import java.math.BigDecimal

class SoldeViewModel(
    private val getSolde: GetSoldeByOperateurEtTypeEtDeviseUseCase,
    private val saveOrUpdateSolde: SaveOrUpdateSoldeUseCase,
    val storageManager: DataStorageManager,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SoldeUiState())
    val uiState = _uiState.asStateFlow()

    private val _soldes = MutableStateFlow<Solde>(value = Solde())
    val soldes = _soldes.asStateFlow()

    private val _uiEvent = MutableSharedFlow<SoldeUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    fun onSoldeChange(solde: String) {
        _uiState.update {
            it.copy(solde = solde, isSoldeError = false)
        }
    }


    init {
        getSoldes()
    }

    fun onOperateurChange(operateur: Operateur) {
        _uiState.update {
            it.copy(selectedOperateur = operateur)
        }
        getSoldes()
    }

    fun onSoldeTypeChange(soldeType: SoldeType) {
        _uiState.update {
            it.copy(selecteSoldeType = soldeType)
        }
        if (!_uiState.value.isBottomSheetShown) {
            getSoldes()
        }

    }

    fun onDeviseChange(devise: Constants.Devise) {
        _uiState.update {
            it.copy(selectedDevise = devise)

        }

        if (!_uiState.value.isBottomSheetShown) {
            getSoldes()
        }
    }




    fun getSoldes() {
        viewModelScope.launch {
            getSolde(
                _uiState.value.selectedOperateur.id,
                _uiState.value.selectedDevise.name,
                _uiState.value.selecteSoldeType
            ).collect { result ->

                when (result) {

                    is Result.Loading -> {
                        _uiState.update {
                            it.copy(isLoading = true)
                        }
                    }

                    is Result.Success -> {
                        _uiState.update {
                            it.copy(isLoading = false)
                        }
                        _soldes.value = result.data ?: Solde()


                    }

                    is Result.Error -> {
                        _uiState.update {
                            it.copy(isLoading = false, errorMessage = result.e?.message)
                        }
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




}

data class SoldeUiState(

    val solde: String = "",
    val selectedOperateur: Operateur = operateurs.first(),
    val selectedDevise: Constants.Devise = Constants.Devise.USD,
    val selecteSoldeType: SoldeType = SoldeType.PHYSIQUE,
    val seuilAlert: String? = null,
    val isSoldeError: Boolean = false,
    val isSeuilError: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isBottomSheetShown: Boolean = false,
    val isOperateurExpanded: Boolean = false,
    val isConfirmDialogShown: Boolean = false
)

sealed class SoldeUiEvent {

    data class ShowSuccesMessage(val successMessage: String) : SoldeUiEvent()
    data class SoldeError(val errorMessage: String) : SoldeUiEvent()

}


