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
import org.megamind.mycashpoint.domain.model.Solde
import org.megamind.mycashpoint.domain.model.TransactionType
import org.megamind.mycashpoint.domain.model.operateurs
import org.megamind.mycashpoint.domain.usecase.agence.GetAgencesUseCase
import org.megamind.mycashpoint.domain.usecase.transaction.GenerateTransactionReportUseCase
import org.megamind.mycashpoint.utils.Constants
import org.megamind.mycashpoint.utils.Result
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId


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
    fun generateReport() {
        viewModelScope.launch {

            val selectedAgence = _uiState.value.selectedAgence ?: return@launch
            val selectedOperateur = _uiState.value.selectedOperateur ?: return@launch
            val selectedDevise = _uiState.value.selectedDevise
            val selectedType = _uiState.value.selectedType
            val startDate =
                _uiState.value.startDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
            val endDate =
                _uiState.value.endDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()


            generateTransactionReport(
                codeAgence = selectedAgence.codeAgence,
                operateurId = selectedOperateur.id, deviseCode = selectedDevise.name,
                type = selectedType, startDate = startDate, endDate = endDate
            ).collect { result ->

                when (result) {
                    is Result.Loading -> {
                        _uiState.update {
                            it.copy(isPdfLoading = true)
                        }
                    }

                    is Result.Success -> {

                        result.data?.let { byte ->
                            _uiState.update {
                                it.copy(isPdfLoading = false, pdfToOpen = listOf(byte))
                            }
                        }

                        val pdfBytes = result.data  // ByteArray
                    }

                    is Result.Error -> {

                        _uiState.update {
                            it.copy(isPdfLoading = false)
                        }
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

    fun onSelectedTransactTypeChange(type: TransactionType) {
        _uiState.update { it.copy(selectedType = type) }

    }

    fun onTransactTypeDropdownExpanded(expanded: Boolean) {
        _uiState.update { it.copy(isTransactTypeDropDownExpanded = expanded) }
    }

    fun clearPdfEvent() {
        _uiState.update { it.copy(pdfToOpen = null) }
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
    val pdfToOpen: List<ByteArray>? = null,
    val isPdfLoading: Boolean = false,

    val startDate: LocalDateTime = LocalDateTime.now().minusDays(30),
    val endDate: LocalDateTime = LocalDateTime.now(),
    val isStartDatePickerShown: Boolean = false,
    val isEndDatePickerShown: Boolean = false,
    val isTransactTypeDropDownExpanded: Boolean = false,

)