package org.megamind.mycashpoint.ui.Agence

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.megamind.mycashpoint.data.data_source.local.entity.Agence
import org.megamind.mycashpoint.domain.repository.AgenceRepository
import org.megamind.mycashpoint.utils.Result

class AgenceViewModel(private val repository: AgenceRepository) : ViewModel() {


    private val _uiState = MutableStateFlow(AgenceUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<AgenceUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()


    private val _agences = MutableStateFlow<List<Agence>>(emptyList())
    val agences = _agences.asStateFlow()


    private val _id get() = _uiState.value.id
    private val _designation get() = _uiState.value.designation

    init {
        getAgences()
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


    fun getAgences() {
        viewModelScope.launch {
            repository.getAll().collect { result ->
                when (result) {
                    Result.Loading -> {
                        _uiState.update {
                            it.copy(isLoadind = true)
                        }

                    }

                    is Result.Succes<*> -> {
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


        if (_id.isEmpty()) {
            _uiState.update {
                it.copy(isIdError = true)
            }
            return
        }

        if (_designation.isEmpty()) {
            _uiState.update {
                it.copy(isDesignationError = true)
            }
            return
        }

        val agence = Agence(id = _id, designation = _designation)

        viewModelScope.launch {

            repository.saveOrUpdate(agence).collect { result ->

                when (result) {
                    Result.Loading -> {
                        _uiState.update {
                            it.copy(isLoadind = true)
                        }
                    }

                    is Result.Succes<*> -> {

                        _uiState.update {
                            it.copy(isLoadind = false)
                        }
                        _uiEvent.emit(AgenceUiEvent.OnSaveOrUpdate)
                        getAgences()


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
