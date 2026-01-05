package org.megamind.mycashpoint.ui.screen.admin.operation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.megamind.mycashpoint.domain.model.Agence
import org.megamind.mycashpoint.domain.model.OperationCaisseType
import org.megamind.mycashpoint.domain.model.Operateur
import org.megamind.mycashpoint.domain.model.operateurs
import org.megamind.mycashpoint.domain.model.SoldeType
import org.megamind.mycashpoint.domain.usecase.agence.GetAgencesUseCase
import org.megamind.mycashpoint.domain.usecase.operation.SaveOperationCaisseUseCase
import org.megamind.mycashpoint.utils.Constants
import org.megamind.mycashpoint.utils.Result
import java.math.BigDecimal


class OperationCaisseViewModel(
    private val saveOperationCaisseUseCase: SaveOperationCaisseUseCase,
    private val getAgencesUseCase: GetAgencesUseCase
) : ViewModel() {

    init {
        getAgences()
    }

    private val _uiState = MutableStateFlow(OperationCaisseUiState())
    val uiState = _uiState.asStateFlow()

    private val _eventChannel = Channel<OperationCaisseEvent>()
    val event = _eventChannel.receiveAsFlow()

    fun onTypeSelected(type: OperationCaisseType) {
        _uiState.update { it.copy(selectedOperationCaisseType = type) }
    }

    fun onOperateurSelected(operateur: Operateur) {
        _uiState.update { it.copy(selectedOperateur = operateur) }
    }

    fun onAmountChange(amount: String) {
        _uiState.update { it.copy(amount = amount) }
    }

    fun onMotifChange(motif: String) {
        _uiState.update { it.copy(motif = motif) }
    }

    fun onCurrencySelected(currency: Constants.Devise) {
        _uiState.update { it.copy(selectedCurrency = currency) }
    }

    fun onSubmit() {
        val currentState = _uiState.value
        
        if (currentState.amount.isBlank() || (currentState.amount.toBigDecimalOrNull()
                ?.compareTo(BigDecimal.ZERO) ?: -1) <= 0
        ) {
            sendEvent(OperationCaisseEvent.ShowError("Veuillez entrer un montant valide"))
            return
        }
        if (currentState.motif.isBlank()) {
            sendEvent(OperationCaisseEvent.ShowError("Veuillez entrer un motif"))
            return
        }

        if (currentState.selectedOperateur == null) {
            sendEvent(OperationCaisseEvent.ShowError("Veuillez sélectionner un opérateur"))
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            saveOperationCaisseUseCase(
                type = currentState.selectedOperationCaisseType,
                montant = currentState.amount,
                motif = currentState.motif,
                devise = currentState.selectedCurrency,
                operateurId = currentState.selectedOperateur.id,
                codAgence = _uiState.value.selectedAgence.codeAgence,
                soldeType = _uiState.value.selectedSoldeType,

                ).collect { result ->

                when (result) {
                    is Result.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                success = true,
                                amount = "",
                                motif = ""
                            )
                        }
                        sendEvent(OperationCaisseEvent.ShowSuccess("Opération effectuée avec succès"))
                    }

                    is Result.Error -> {
                        _uiState.update { it.copy(isLoading = false, error = result.e?.message) }
                        sendEvent(
                            OperationCaisseEvent.ShowError(
                                result.e?.message ?: "Une erreur est survenue"
                            )
                        )
                    }

                    is Result.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                }
            }
        }
    }

    private fun sendEvent(event: OperationCaisseEvent) {
        viewModelScope.launch {
            _eventChannel.send(event)
        }
    }


    fun onSelectedAgenceChange(agence: Agence) {
        _uiState.update { it.copy(selectedAgence = agence) }
    }

    fun onSelectedSoldeTypeChange(soldeType: SoldeType) {
        _uiState.update { it.copy(selectedSoldeType = soldeType) }
    }

    fun onAgenceDropdownExpandChange(expanded: Boolean) {
        _uiState.update { it.copy(isAgenceExpanded = expanded) }
    }

    fun onSoldeTypeDropdownExpandChange(expanded: Boolean) {
        _uiState.update { it.copy(isSoldeTypeExpanded = expanded) }
    }

    fun onOperateurDropdownExpandChange(expanded: Boolean) {
        _uiState.update { it.copy(isOperateurExpanded = expanded) }
    }

    private fun getAgences() {
        viewModelScope.launch {
            getAgencesUseCase().collect { result ->
                when (result) {
                    is Result.Success -> {
                        val agences = result.data ?: emptyList()
                        _uiState.update { 
                            it.copy(
                                agences = agences,
                                selectedAgence = if (agences.isNotEmpty()) agences.first() else it.selectedAgence
                            ) 
                        }
                    }

                    is Result.Error -> {
                        // Handle error locally or show a message if needed
                        sendEvent(OperationCaisseEvent.ShowError("Erreur lors du chargement des agences"))
                    }

                    is Result.Loading -> {
                        // Optional: Handle loading specific to agencies if separate loader is needed
                    }
                }
            }
        }
    }

}


data class OperationCaisseUiState(
    val selectedOperationCaisseType: OperationCaisseType = OperationCaisseType.APPROVISIONNEMENT,
    val selectedSoldeType: SoldeType = SoldeType.PHYSIQUE,
    val selectedOperateur: Operateur? = null,
    val amount: String = "",
    val motif: String = "",
    val selectedCurrency: Constants.Devise = Constants.Devise.USD,
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false,
    val selectedAgence: Agence = Agence(),
    val isAgenceExpanded: Boolean = false,
    val isSoldeTypeExpanded: Boolean = false,
    val isOperateurExpanded: Boolean = false,
    val agences: List<Agence> = emptyList(),

)

sealed class OperationCaisseEvent {
    data class ShowError(val message: String) : OperationCaisseEvent()
    data class ShowSuccess(val message: String) : OperationCaisseEvent()
}