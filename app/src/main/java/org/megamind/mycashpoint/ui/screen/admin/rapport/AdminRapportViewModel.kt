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
import org.megamind.mycashpoint.domain.usecase.excel.GetGrandLivreExcelUseCase
import org.megamind.mycashpoint.domain.usecase.excel.GetJournalOperationInterneExcelUseCase
import org.megamind.mycashpoint.domain.usecase.excel.GetJournalTransactionExcelUseCase
import org.megamind.mycashpoint.domain.usecase.transaction.GenerateTransactionReportUseCase
import org.megamind.mycashpoint.utils.Constants
import org.megamind.mycashpoint.utils.Result
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId


class AdminRapportViewModel(
    private val getAgencesUseCase: GetAgencesUseCase,
    private val generateTransactionReport: GenerateTransactionReportUseCase,
    private val getGrandLivreExcelUseCase: GetGrandLivreExcelUseCase,
    private val getJournalTransactionExcelUseCase: GetJournalTransactionExcelUseCase,
    private val getJournalOperationInterneExcelUseCase: GetJournalOperationInterneExcelUseCase
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


    fun onSelectedAgence(agence: Agence) {
        _uiState.value =
            _uiState.value.copy(selectedAgence = agence, isAgenceDropDownExpanded = false)

    }


    fun onAgenceDropdownExpanded(expanded: Boolean) {
        _uiState.update { it.copy(isAgenceDropDownExpanded = expanded) }
    }


    // Report Dialog functions
    fun onReportClick(report: Rapports) {
        _uiState.update {
            it.copy(
                selectedReportType = report,
                isReportDialogShown = true,
                // Reset dialog dates to default values
                dialogStartDate = LocalDateTime.now().minusDays(30),
                dialogEndDate = LocalDateTime.now(),
                isDateFilterEnabled = false
            )
        }
    }

    fun onDismissReportDialog() {
        _uiState.update { it.copy(isReportDialogShown = false, selectedReportType = null) }
    }

    fun onDateFilterEnabledChange(enabled: Boolean) {
        _uiState.update { it.copy(isDateFilterEnabled = enabled) }
    }

    fun onDialogStartDateChange(date: LocalDate) {
        _uiState.update { it.copy(dialogStartDate = date.atStartOfDay()) }
    }

    fun onDialogEndDateChange(date: LocalDate) {
        _uiState.update { it.copy(dialogEndDate = date.atStartOfDay()) }
    }

    fun onShowDialogStartDatePicker() {
        _uiState.update { it.copy(isDialogStartDatePickerShown = true) }
    }

    fun onDismissDialogStartDatePicker() {
        _uiState.update { it.copy(isDialogStartDatePickerShown = false) }
    }

    fun onShowDialogEndDatePicker() {
        _uiState.update { it.copy(isDialogEndDatePickerShown = true) }
    }

    fun onDismissDialogEndDatePicker() {
        _uiState.update { it.copy(isDialogEndDatePickerShown = false) }
    }

    fun onConfirmReportGeneration() {
        val selectedReport = _uiState.value.selectedReportType ?: return
        val selectedAgence = _uiState.value.selectedAgence ?: return
        val isDateFilterEnabled = _uiState.value.isDateFilterEnabled

        val startDate = if (isDateFilterEnabled) {
            _uiState.value.dialogStartDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        } else null
        val endDate = if (isDateFilterEnabled) {
            _uiState.value.dialogEndDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        } else null

        // Close dialog
        _uiState.update { it.copy(isReportDialogShown = false) }

        Log.i(
            TAG,
            "Generating report: ${selectedReport.label} for agence: ${selectedAgence.designation}"
        )
        Log.i(
            TAG,
            "Date filter enabled: $isDateFilterEnabled, startDate: $startDate, endDate: $endDate"
        )

        // Generate Excel report based on selected report type
        when (selectedReport) {
            Rapports.GRAND_LIVRE -> generateGrandLivreExcel(
                selectedAgence.codeAgence,
                startDate,
                endDate
            )

            Rapports.JOURNAL_TRANSACT -> generateJournalTransactionExcel(
                selectedAgence.codeAgence,
                startDate,
                endDate
            )

            Rapports.JOURNAL_OPERATION -> generateJournalOperationInterneExcel(
                selectedAgence.codeAgence,
                startDate,
                endDate
            )
        }
    }

    private fun generateGrandLivreExcel(codeAgence: String, startDate: Long?, endDate: Long?) {
        viewModelScope.launch {
            getGrandLivreExcelUseCase(codeAgence, startDate, endDate).collect { result ->
                handleExcelResult(result, "Grand Livre")
            }
        }
    }

    private fun generateJournalTransactionExcel(
        codeAgence: String,
        startDate: Long?,
        endDate: Long?
    ) {
        viewModelScope.launch {
            getJournalTransactionExcelUseCase(codeAgence, startDate, endDate).collect { result ->
                handleExcelResult(result, "Journal Transaction")
            }
        }
    }

    private fun generateJournalOperationInterneExcel(
        codeAgence: String,
        startDate: Long?,
        endDate: Long?
    ) {
        viewModelScope.launch {
            getJournalOperationInterneExcelUseCase(
                codeAgence,
                startDate,
                endDate
            ).collect { result ->
                handleExcelResult(result, "Journal Opération Interne")
            }
        }
    }

    private fun handleExcelResult(result: Result<ByteArray>, reportName: String) {
        when (result) {
            is Result.Loading -> {
                _uiState.update { it.copy(isExcelLoading = true) }
                Log.i(TAG, "$reportName Excel: Loading...")
            }

            is Result.Success -> {
                result.data?.let { bytes ->
                    _uiState.update {
                        it.copy(
                            isExcelLoading = false,
                            excelToOpen = listOf(bytes),
                            excelErrorMessage = null
                        )
                    }
                    Log.i(TAG, "$reportName Excel: Success - ${bytes.size} bytes")
                }
            }

            is Result.Error<*> -> {
                _uiState.update {
                    it.copy(
                        isExcelLoading = false,
                        excelErrorMessage = result.e?.message ?: "Erreur inconnue"
                    )
                }
                Log.e(TAG, "$reportName Excel: Error - ${result.e?.message}")
            }
        }
    }

    fun clearExcelEvent() {
        _uiState.update { it.copy(excelToOpen = null) }
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


    val startDate: LocalDateTime = LocalDateTime.now().minusDays(30),
    val endDate: LocalDateTime = LocalDateTime.now(),
    val isStartDatePickerShown: Boolean = false,
    val isEndDatePickerShown: Boolean = false,
    val isTransactTypeDropDownExpanded: Boolean = false,

    // Report Dialog State
    val selectedReportType: Rapports? = null,
    val isReportDialogShown: Boolean = false,
    val isDateFilterEnabled: Boolean = false,
    val dialogStartDate: LocalDateTime = LocalDateTime.now().minusDays(30),
    val dialogEndDate: LocalDateTime = LocalDateTime.now(),
    val isDialogStartDatePickerShown: Boolean = false,
    val isDialogEndDatePickerShown: Boolean = false,

    // PDF State (existing)
    val isPdfLoading: Boolean = false,
    val pdfToOpen: List<ByteArray>? = null,

    // Excel State
    val isExcelLoading: Boolean = false,
    val excelToOpen: List<ByteArray>? = null,
    val excelErrorMessage: String? = null,

    )