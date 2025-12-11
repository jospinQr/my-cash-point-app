package org.megamind.mycashpoint.ui.screen.admin.rapport

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.megamind.mycashpoint.domain.model.Agence
import org.megamind.mycashpoint.domain.model.Operateur
import org.megamind.mycashpoint.domain.model.TransactionType
import org.megamind.mycashpoint.domain.model.operateurs
import org.megamind.mycashpoint.domain.usecase.agence.GetAgencesUseCase
import org.megamind.mycashpoint.domain.usecase.transaction.GenerateTransactionReportUseCase
import org.megamind.mycashpoint.utils.Constants
import org.megamind.mycashpoint.utils.Result
import java.time.LocalDate
import java.time.LocalDateTime

class AdminRapportViewModel(
    private val getAgencesUseCase: GetAgencesUseCase,
    private val generateTransactionReport: GenerateTransactionReportUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminRepportUiState())
    val uiState = _uiState.asStateFlow()
    private val TAG = "AdminRapportViewModel"
    private var hasLoadedAgences = false


    init {
        getAllAgence()
    }

    fun generateReport() {
        viewModelScope.launch {

            val selectedAgence = _uiState.value.selectedAgence ?: return@launch
            val selectedOperateur = _uiState.value.selectedOperateur ?: return@launch
            val selectedDevise = _uiState.value.selectedDevise
            val selectedType = _uiState.value.selectedType

            generateTransactionReport(
                codeAgence = selectedAgence.codeAgence,
                operateurId = selectedOperateur.id, deviseCode = selectedDevise.name,
                type = selectedType, startDate = null, endDate = null
            ).collect {






            }


        }

    }

    fun getAllAgence() {

        viewModelScope.launch {
            getAgencesUseCase().collect { result ->
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

    fun onShowStartDatePicker() {
        _uiState.update { it.copy(isStartDatePickerShown = true) }
    }

    fun onDismissStartDatePicker() {
        _uiState.update { it.copy(isStartDatePickerShown = false) }
    }

    fun onShowEndDatePicker() {
        _uiState.update { it.copy(isEndDatePickerShown = true) }
    }

    fun onDismissEndDatePicker() {
        _uiState.update { it.copy(isEndDatePickerShown = false) }
    }

    fun onStartDateChange(date: LocalDate) {
        _uiState.value = uiState.value.copy(startDate = date.atStartOfDay())

    }

    fun onEndDateChange(date: LocalDate) {
        _uiState.value = uiState.value.copy(endDate = date.atStartOfDay())
    }


}

data class AdminRepportUiState(

    val agenceList: List<Agence> = emptyList(),
    val selectedAgence: Agence? = null,
    val isAgenceLoading: Boolean = false,
    val agenceErrorMessage: String? = null,
    val isAgenceDropDownExpanded: Boolean = false,
    val selectedOperateur: Operateur? = operateurs.firstOrNull(),
    val selectedDevise: Constants.Devise = Constants.Devise.USD,
    val selectedType: TransactionType = TransactionType.DEPOT,

    val startDate: LocalDateTime = LocalDateTime.now().minusDays(30),
    val endDate: LocalDateTime = LocalDateTime.now(),
    val isStartDatePickerShown: Boolean = false,
    val isEndDatePickerShown: Boolean = false,
)