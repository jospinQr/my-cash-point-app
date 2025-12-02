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
import org.megamind.mycashpoint.domain.usecase.agence.GetAgencesUseCase
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


    fun onAgenceDropdownExpanded(expanded: Boolean) {
        _uiState.update { it.copy(isAgenceDropDownExpanded = expanded) }
    }


}


data class DashBoardUiState(
    val agenceList: List<Agence> = emptyList(),
    val selectedAgence: Agence? = null,
    val isAgenceLoading: Boolean = false,
    val agenceErrorMessage: String? = null,
    val isAgenceDropDownExpanded: Boolean = false,
)