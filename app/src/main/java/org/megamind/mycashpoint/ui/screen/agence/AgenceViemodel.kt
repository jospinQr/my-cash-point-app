package org.megamind.mycashpoint.ui.screen.agence

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.megamind.mycashpoint.domain.model.Agence
import org.megamind.mycashpoint.domain.usecase.agence.GetAgencesUseCase
import org.megamind.mycashpoint.domain.usecase.agence.SaveOrUpdateAgenceUseCase
import org.megamind.mycashpoint.domain.usecase.agence.AgenceValidationException
import org.megamind.mycashpoint.domain.usecase.agence.AgenceField
import org.megamind.mycashpoint.utils.Result

class AgenceViewModel(
    private val getAgences: GetAgencesUseCase,
    private val saveOrUpdateAgence: SaveOrUpdateAgenceUseCase
) : ViewModel() {


    private val _uiState = MutableStateFlow(AgenceUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<AgenceUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()


    private val _agences = MutableStateFlow<List<Agence>>(emptyList())
    val agences = _agences.asStateFlow()


    private val _id get() = _uiState.value.id
    private val _designation get() = _uiState.value.designation

    init {
        getAgence()
    }

    fun onIdChange(value: String) {
        _uiState.update {
            it.copy(id = value, isIdError = false)
        }
    }

    fun onDesignationChange(value: String) {
        _uiState.update {
            it.copy(designation = value, isDesignationError = false)
        }

    }


    fun getAgence() {
        viewModelScope.launch {
            getAgences().collect { result ->
                when (val result = result) {
                    Result.Loading -> {
                        _uiState.update {
                            it.copy(isLoadind = true)
                        }

                    }

                    is Result.Success<*> -> {
                        _uiState.update {
                            it.copy(isLoadind = false)
                        }
                        _agences.update {
                            result.data as List<Agence>
                        }
                    }

                    is Result.Error<*> -> {
                        _uiState.update {
                            it.copy(isLoadind = false, error = result.e?.message)
                        }
                    }


                }
            }
        }
    }

    fun onSaveOrUpdate() {
        val agence = Agence(codeAgence = _id, designation = _designation)

        viewModelScope.launch {
            saveOrUpdateAgence(agence).collect { result ->

                when (result) {
                    Result.Loading -> {
                        _uiState.update {
                            it.copy(isLoadind = true)
                        }
                    }

                    is Result.Success<*> -> {

                        _uiState.update {
                            it.copy(isLoadind = false, isFomShown = false)
                        }
                        _uiEvent.emit(AgenceUiEvent.OnSaveOrUpdate)
                        getAgence()


                    }

                    is Result.Error<*> -> {
                        val ex = result.e
                        when (ex) {
                            is AgenceValidationException.FieldRequired -> {
                                _uiState.update {
                                    when (ex.field) {
                                        AgenceField.ID -> it.copy(
                                            isLoadind = false,
                                            isIdError = true
                                        )

                                        AgenceField.DESIGNATION -> it.copy(
                                            isLoadind = false,
                                            isDesignationError = true
                                        )
                                    }
                                }
                            }

                            else -> {
                                _uiState.update {
                                    it.copy(isLoadind = false, error = ex?.message)
                                }
                            }
                        }
                    }

                }

            }

        }
    }


    fun onFormShown() {
        _uiState.update {
            it.copy(isFomShown = true)
        }
    }

    fun onFormHidden() {
        _uiState.update {
            it.copy(isFomShown = false)
        }
    }


}

data class AgenceUiState(
    val id: String = "",
    val designation: String = "",

    val isLoadind: Boolean = false,
    val error: String? = null,
    val isFomShown: Boolean = false,


    val isIdError: Boolean = false,
    val isDesignationError: Boolean = false,

    )

sealed class AgenceUiEvent {

    object OnSaveOrUpdate : AgenceUiEvent()


}
