package org.megamind.mycashpoint.ui.screen.admin.dash_board

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.megamind.mycashpoint.domain.model.Agence
import org.megamind.mycashpoint.domain.model.Operateur
import org.megamind.mycashpoint.domain.model.SoldeType
import org.megamind.mycashpoint.domain.model.TopOperateur
import org.megamind.mycashpoint.domain.model.operateurs
import org.megamind.mycashpoint.domain.usecase.agence.GetAgencesUseCase
import org.megamind.mycashpoint.utils.Constants
import org.megamind.mycashpoint.utils.Result

class DashBoardViewModel(private val getAllAgenceUseCase: GetAgencesUseCase) : ViewModel() {


    private val _uiState = MutableStateFlow(DashBoardUiState())
    val uiState = _uiState.asStateFlow()
    private var hasLoadedAgences = false

    fun getAllAgence() {

        if (hasLoadedAgences) return

        getAllAgenceUseCase()
            .onStart { _uiState.value = _uiState.value.copy(isAgenceLoading = true) }
            .catch { e ->
                _uiState.value = _uiState.value.copy(
                    isAgenceLoading = false,
                    agenceErrorMessage = e.message ?: "Erreur inconnue"
                )
            }
            .onEach { result ->
                _uiState.value = when (result) {
                    is Result.Success -> {
                        hasLoadedAgences = true
                        _uiState.value.copy(
                            isAgenceLoading = false,
                            agenceList = result.data ?: emptyList(),
                            agenceErrorMessage = null,
                            selectedAgence = result.data?.firstOrNull()
                        )


                    }

                    else -> _uiState.value.copy(isAgenceLoading = false)
                }
            }
            .launchIn(viewModelScope)
    }


    fun onSelectedAgence(agence: Agence) {
        _uiState.value =
            _uiState.value.copy(selectedAgence = agence, isAgenceDropDownExpanded = false)
    }

    fun onSelectedDeviseChange(devise: Constants.Devise) {
        _uiState.update { it.copy(selectedDevise = devise) }
    }


    fun onAgenceDropdownExpanded(expanded: Boolean) {
        _uiState.update { it.copy(isAgenceDropDownExpanded = expanded) }
    }

    fun onSelectedOperateurChange(operateur: Operateur) {
        _uiState.update { it.copy(selectedOperateur = operateur) }
    }

    fun onSelectedSoldeTypeChange(soldeType: SoldeType) {
        _uiState.update { it.copy(selectedSoldeType = soldeType) }
    }


}


data class DashBoardUiState(
    val agenceList: List<Agence> = emptyList(),
    val selectedAgence: Agence? = null,
    val isAgenceLoading: Boolean = false,
    val agenceErrorMessage: String? = null,
    val isAgenceDropDownExpanded: Boolean = false,
    val selectedOperateur: Operateur? = operateurs.firstOrNull(),
    val selectedDevise: Constants.Devise = Constants.Devise.USD,
    val selectedSoldeType: SoldeType = SoldeType.PHYSIQUE,

    val topOperateur: List<TopOperateur> = listOf(
        TopOperateur(
            operateurNom = "Airtel Money",
            montantTotal = 10000.toBigDecimal(),
            nombreTransactions = 10,
            operateurId = 1,
            devise = "CDF"
        ),
        TopOperateur(
            operateurNom = "Orange Money",
            montantTotal = 10000.toBigDecimal(),
            nombreTransactions = 13,
            operateurId = 1,
            devise = "CDF"
        ),

        TopOperateur(
            operateurNom = "Vodacom M-Pesa",
            montantTotal = 10000.toBigDecimal(),
            nombreTransactions = 4,
            operateurId = 1,
            devise = "CDF"
        ),

        TopOperateur(
            operateurNom = "Equity",
            montantTotal = 10000.toBigDecimal(),
            nombreTransactions = 10,
            operateurId = 1,
            devise = "CDF"
        ),
    )
)