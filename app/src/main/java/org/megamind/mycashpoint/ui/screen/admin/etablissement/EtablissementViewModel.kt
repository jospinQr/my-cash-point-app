package org.megamind.mycashpoint.ui.screen.admin.etablissement

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.megamind.mycashpoint.domain.model.Etablissement
import org.megamind.mycashpoint.domain.usecase.etablissement.GetEtablissementFromServerUseCase
import org.megamind.mycashpoint.domain.usecase.etablissement.UpdateEtablissementUseCase
import org.megamind.mycashpoint.utils.Result

class EtablissementViewModel(
    private val getEtablissementFromServerUseCase: GetEtablissementFromServerUseCase,
    private val updateEtablissementUseCase: UpdateEtablissementUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(EtablissementUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<EtablissementUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    init {
        getEtablissement()
    }

    fun getEtablissement() {
        viewModelScope.launch {
            getEtablissementFromServerUseCase().collect { result ->
                when (result) {
                    is Result.Loading -> _uiState.update { it.copy(isLoading = true) }
                    is Result.Success -> {
                        result.data?.let { etablissement ->
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    etablissement = etablissement,
                                    name = etablissement.name,
                                    address = etablissement.addrees,
                                    contact = etablissement.contat ?: "",
                                    rccm = etablissement.rccm ?: ""
                                )
                            }
                        }
                    }

                    is Result.Error -> _uiState.update {
                        it.copy(isLoading = false, error = result.e?.message ?: "Erreur inconnue")
                    }
                }
            }
        }
    }

    fun onNameChange(name: String) = _uiState.update { it.copy(name = name) }
    fun onAddressChange(address: String) = _uiState.update { it.copy(address = address) }
    fun onContactChange(contact: String) = _uiState.update { it.copy(contact = contact) }
    fun onRccmChange(rccm: String) = _uiState.update { it.copy(rccm = rccm) }

    fun onUpdate() {
        val currentState = _uiState.value
        val etablissement = currentState.etablissement?.copy(
            name = currentState.name,
            addrees = currentState.address,
            contat = currentState.contact,
            rccm = currentState.rccm
        ) ?: return

        viewModelScope.launch {
            updateEtablissementUseCase(etablissement).collect { result ->
                when (result) {
                    is Result.Loading -> _uiState.update { it.copy(isLoading = true) }
                    is Result.Success -> {
                        _uiState.update { it.copy(isLoading = false) }
                        _uiEvent.emit(EtablissementUiEvent.OnUpdateSuccess)
                    }

                    is Result.Error -> _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = result.e?.message ?: "Erreur de mise à jour"
                        )
                    }
                }
            }
        }
    }
}

data class EtablissementUiState(
    val etablissement: Etablissement? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val name: String = "",
    val address: String = "",
    val contact: String = "",
    val rccm: String = ""
)

sealed class EtablissementUiEvent {
    object OnUpdateSuccess : EtablissementUiEvent()
}
