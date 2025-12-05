package org.megamind.mycashpoint.ui.screen.admin.dash_board

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.megamind.mycashpoint.domain.model.Agence
import org.megamind.mycashpoint.domain.model.Operateur
import org.megamind.mycashpoint.domain.model.Solde
import org.megamind.mycashpoint.domain.model.SoldeType
import org.megamind.mycashpoint.domain.model.TopOperateur
import org.megamind.mycashpoint.domain.model.operateurs
import org.megamind.mycashpoint.domain.usecase.agence.GetAgencesUseCase
import org.megamind.mycashpoint.domain.usecase.solde.GetSoldeFromServerByCreteriaUseCase
import org.megamind.mycashpoint.domain.usecase.transaction.GetTopOperateurUseCase
import org.megamind.mycashpoint.domain.usecase.transaction.GetTransactionsByCriteriaUseCase
import org.megamind.mycashpoint.ui.screen.caisse.SoldeScreen
import org.megamind.mycashpoint.ui.screen.rapport.DialogState
import org.megamind.mycashpoint.utils.Constants
import org.megamind.mycashpoint.utils.Result

class DashBoardViewModel(
    private val getAllAgenceUseCase: GetAgencesUseCase,
    private val getSoldeByCriteriaUseCase: GetSoldeFromServerByCreteriaUseCase,
    private val getTopOperateurUseCase: GetTopOperateurUseCase
) : ViewModel() {


    private val TAG = "DashBoardViewModel"
    private val _uiState = MutableStateFlow(DashBoardUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<DashBoardUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()


    private var hasLoadedAgences = false


    init {
        getAllAgence()
    }
    fun getAllAgence() {

        viewModelScope.launch {
            getAllAgenceUseCase().collect { result ->
                when (result) {

                    is Result.Loading -> {
                        _uiState.update {
                            it.copy(isAgenceLoading = true)
                        }
                        Log.i(TAG, "Agence Loading")

                    }

                    is Result.Success -> {

                        hasLoadedAgences = true
                        result.data?.let {
                            _uiState.update {
                                it.copy(
                                    isAgenceLoading = false,
                                    agenceList = result.data,
                                    agenceErrorMessage = null,
                                    selectedAgence = result.data.firstOrNull()
                                )
                            }
                            getSoldeByCriteria()
                            getTopOperateur()

                        }


                        Log.i(TAG, "Agence : ${result.data}")

                    }

                    is Result.Error -> {
                        _uiState.update {
                            it.copy(
                                agenceErrorMessage = result.e?.message ?: "",
                                isAgenceLoading = false
                            )
                        }



                        Log.i(TAG, "Agence Error : ${result.e?.message}")

                    }


                }
            }

        }
    }

    fun getSoldeByCriteria() {
        val agence = _uiState.value.selectedAgence ?: return
        val operateur = _uiState.value.selectedOperateur ?: return
        val devise = _uiState.value.selectedDevise
        val type = _uiState.value.selectedSoldeType

        viewModelScope.launch {
            getSoldeByCriteriaUseCase(
                codeAgence = agence.codeAgence,
                operateurId = operateur.id.toLong(),
                deviseCode = devise.name,
                soldeType = type
            )
                .collect { result ->

                    when (result) {

                        is Result.Loading -> {
                            _uiState.update {
                                it.copy(
                                    isSoldeLoading = true,
                                )
                            }
                        }

                        is Result.Success -> {
                            _uiState.update {
                                it.copy(
                                    isSoldeLoading = false,
                                    soldeErrorMessage = null,
                                    currenteSolde = result.data

                                )
                            }
                        }

                        is Result.Error -> {
                            _uiState.update {
                                it.copy(
                                    isSoldeLoading = false,
                                    soldeErrorMessage = result.e?.message ?: "Erreur inconnue"
                                )
                            }
                        }


                    }
                }
        }
    }


    fun getTopOperateur() {
        val agence = _uiState.value.selectedAgence ?: return
        val devise = _uiState.value.selectedDevise

        viewModelScope.launch {
            getTopOperateurUseCase(
                codeAgence = agence.codeAgence,
                devise = devise
            ).collect { result ->

                when (result) {
                    is Result.Loading -> {


                        _uiState.update {
                            it.copy(
                                isTopOperateurLoading = true,
                            )
                        }

                    }

                    is Result.Success -> {
                        _uiState.update {
                            it.copy(
                                isTopOperateurLoading = false,
                                topOperateur = result.data ?: emptyList()
                            )
                        }

                    }

                    is Result.Error -> {
                        _uiState.update {
                            it.copy(
                                isTopOperateurLoading = false,
                                topOperateurErrorMessage = result.e?.message ?: "Erreur inconnue"
                            )
                        }
                    }
                }


            }
        }

    }

    fun onSelectedAgence(agence: Agence) {
        _uiState.value =
            _uiState.value.copy(selectedAgence = agence, isAgenceDropDownExpanded = false)
        getSoldeByCriteria()
        getTopOperateur()

    }

    fun onSelectedDeviseChange(devise: Constants.Devise) {
        _uiState.update { it.copy(selectedDevise = devise) }
        getSoldeByCriteria()
        getTopOperateur()


    }


    fun onAgenceDropdownExpanded(expanded: Boolean) {
        _uiState.update { it.copy(isAgenceDropDownExpanded = expanded) }
    }

    fun onSelectedOperateurChange(operateur: Operateur) {
        _uiState.update { it.copy(selectedOperateur = operateur) }
        getSoldeByCriteria()
    }

    fun onSelectedSoldeTypeChange(soldeType: SoldeType) {
        _uiState.update { it.copy(selectedSoldeType = soldeType) }
        getSoldeByCriteria()
    }


}


data class DashBoardUiState(
    val agenceList: List<Agence> = emptyList(),
    val selectedAgence: Agence? = null,

    val isAgenceLoading: Boolean = false,
    val isSoldeLoading: Boolean = false,
    val isTopOperateurLoading: Boolean = false,

    val agenceErrorMessage: String? = null,
    val soldeErrorMessage: String? = null,
    val topOperateurErrorMessage: String? = null,
    val isAgenceDropDownExpanded: Boolean = false,

    val selectedOperateur: Operateur? = operateurs.firstOrNull(),
    val selectedDevise: Constants.Devise = Constants.Devise.USD,
    val selectedSoldeType: SoldeType = SoldeType.PHYSIQUE,
    val currenteSolde: Solde? = null,
    val topOperateur: List<TopOperateur> = emptyList()
)


sealed class DashBoardUiEvent {

    object NavigateToCreateAgence : DashBoardUiEvent()

    object NavigateToCreateAgent : DashBoardUiEvent()


}
