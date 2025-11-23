package org.megamind.mycashpoint.ui.screen.rapport

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.megamind.mycashpoint.data.data_source.local.entity.TransactionEntity
import org.megamind.mycashpoint.data.data_source.local.mapper.toTransaction
import org.megamind.mycashpoint.domain.model.Operateur
import org.megamind.mycashpoint.domain.model.TransactionType
import org.megamind.mycashpoint.domain.model.operateurs
import org.megamind.mycashpoint.domain.usecase.rapport.GetTransactionsByOperatorAndDeviceUseCase
import org.megamind.mycashpoint.domain.usecase.transaction.DeleteTransactionUseCase
import org.megamind.mycashpoint.domain.usecase.transaction.SendOneTransactToServerUseCase
import org.megamind.mycashpoint.domain.usecase.transaction.TransactionField
import org.megamind.mycashpoint.domain.usecase.transaction.TransactionValidationException
import org.megamind.mycashpoint.domain.usecase.transaction.UpdateTransactionUseCase
import org.megamind.mycashpoint.ui.screen.main.utils.Constants
import org.megamind.mycashpoint.ui.screen.main.utils.Result
import java.math.BigDecimal

class RapportViewModel(
    private val getTransactionByOperateurAndDeviseUseCase: GetTransactionsByOperatorAndDeviceUseCase,
    private val deleteTransactionUseCase: DeleteTransactionUseCase,
    private val updateTransactionUseCase: UpdateTransactionUseCase,
    private val sendOneTransactToServerUseCase: SendOneTransactToServerUseCase

) : ViewModel() {


    private val _uiState = MutableStateFlow(RapportUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<RapportUiEvent>()
    val uiEvent: SharedFlow<RapportUiEvent> = _uiEvent.asSharedFlow()

    private val _cachedTransactions = MutableStateFlow<List<TransactionEntity>>(emptyList())
    private val _transactionByOperateurAndDevise =
        MutableStateFlow<List<TransactionEntity>>(emptyList())
    val transaction = _transactionByOperateurAndDevise.asStateFlow()

    private val _selectedDevise get() = uiState.value.selectedDevise
    private val _selectedOperateur get() = uiState.value.selectedOperateur


    fun onSearchValueChange(value: String) {
        _uiState.update {
            it.copy(searchValue = value)
        }
        filterTransactions()
    }

    fun onSelectedOperateurChange(operateur: Operateur) {

        _uiState.update {
            it.copy(selectedOperateur = operateur)
        }
        filterTransactions()
        gettransactionByOperateurAndDevise()

    }

    fun onSelectedDeviseChange(devise: Constants.Devise) {
        _uiState.update {
            it.copy(selectedDevise = devise)
        }

        filterTransactions()
        gettransactionByOperateurAndDevise()
    }

    fun onTransactionClick(transaction: TransactionEntity) {
        _uiState.update {
            it.copy(
                selectedTransaction = transaction,
                isTransactionDialogVisible = true,
                isEditSheetVisible = false,
                isDeleteConfirmationVisible = false
            )
        }
    }

    fun onTransactionDialogDismiss() {
        _uiState.update {
            it.copy(
                selectedTransaction = null,
                isTransactionDialogVisible = false,
                isDeleteConfirmationVisible = false,
                isEditSheetVisible = false,
                editErrorMessage = ""
            )
        }
    }

    fun onDeleteTransactionRequest(transaction: TransactionEntity) {
        _uiState.update {
            it.copy(
                selectedTransaction = transaction,
                isDeleteConfirmationVisible = true
            )
        }
    }

    fun onDeleteTransactionCancel() {
        _uiState.update {
            it.copy(
                isDeleteConfirmationVisible = false
            )
        }
    }


    fun onDeleteTransactionConfirm() {
        val transaction = uiState.value.selectedTransaction ?: return

        viewModelScope.launch {
            deleteTransactionUseCase(transaction.id).collect { result ->
                when (result) {
                    is Result.Loading -> {
                        _uiState.update {
                            it.copy(isLoading = true, errorMessage = "")
                        }
                    }

                    is Result.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                isDeleteConfirmationVisible = false,
                                isTransactionDialogVisible = false,
                                selectedTransaction = null
                            )
                        }
                        _uiEvent.emit(RapportUiEvent.ShowSuccesMessage("Transaction supprimée"))
                        gettransactionByOperateurAndDevise()
                    }

                    is Result.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                isDeleteConfirmationVisible = false,
                                errorMessage = result.e?.message ?: "Erreur lors de la suppression"
                            )
                        }
                    }
                }
            }
        }
    }

    fun onEditTransactionRequest(transaction: TransactionEntity) {
        _uiState.update {
            it.copy(
                selectedTransaction = transaction,
                isTransactionDialogVisible = false,
                isDeleteConfirmationVisible = false,
                isEditSheetVisible = true,
                editSelectedType = transaction.type,
                editSelectedDevise = transaction.device,
                editMontant = transaction.montant.stripTrailingZeros().toPlainString(),
                editNomClient = transaction.nomClient.orEmpty(),
                editTelephoneClient = transaction.numClient.orEmpty(),
                editNomBeneficiaire = transaction.nomBeneficaire.orEmpty(),
                editTelephoneBeneficiaire = transaction.numBeneficaire.orEmpty(),
                editNote = transaction.note.orEmpty(),
                isEditMontantError = false,
                isEditNomClientError = false,
                isEditTelephoneClientError = false,
                isEditNomBeneficiaireError = false,
                isEditTelephoneBeneficiaireError = false,
                editErrorMessage = ""
            )
        }
    }

    fun onEditSheetDismiss() {
        _uiState.update {
            it.copy(
                isEditSheetVisible = false,
                editErrorMessage = "",
                isEditMontantError = false,
                isEditNomClientError = false,
                isEditTelephoneClientError = false,
                isEditNomBeneficiaireError = false,
                isEditTelephoneBeneficiaireError = false
            )
        }
    }

    fun onEditMontantChange(value: String) {
        _uiState.update {
            it.copy(editMontant = value, isEditMontantError = false, editErrorMessage = "")
        }
    }

    fun onEditNomClientChange(value: String) {
        _uiState.update {
            it.copy(editNomClient = value, isEditNomClientError = false, editErrorMessage = "")
        }
    }

    fun onEditTelephoneClientChange(value: String) {
        _uiState.update {
            it.copy(
                editTelephoneClient = value,
                isEditTelephoneClientError = false,
                editErrorMessage = ""
            )
        }
    }

    fun onEditNomBeneficiaireChange(value: String) {
        _uiState.update {
            it.copy(
                editNomBeneficiaire = value,
                isEditNomBeneficiaireError = false,
                editErrorMessage = ""
            )
        }
    }

    fun onEditTelephoneBeneficiaireChange(value: String) {
        _uiState.update {
            it.copy(
                editTelephoneBeneficiaire = value,
                isEditTelephoneBeneficiaireError = false,
                editErrorMessage = ""
            )
        }
    }

    fun onEditNoteChange(value: String) {
        _uiState.update { it.copy(editNote = value) }
    }

    fun onEditDeviseChange(devise: Constants.Devise) {
        _uiState.update { it.copy(editSelectedDevise = devise) }
    }

    fun onEditTypeChange(type: TransactionType) {
        _uiState.update {
            it.copy(
                editSelectedType = type,
                isEditNomBeneficiaireError = false,
                isEditTelephoneBeneficiaireError = false
            )
        }
    }

    fun onEditTransactionSubmit() {
        val currentState = uiState.value
        val transaction = currentState.selectedTransaction ?: return

        val montant = currentState.editMontant.trim().replace(',', '.').toBigDecimalOrNull()
        if (montant == null || montant <= BigDecimal.ZERO) {
            _uiState.update {
                it.copy(isEditMontantError = true, editErrorMessage = "")
            }
            return
        }

        val updatedTransaction = transaction.toTransaction().copy(
            type = currentState.editSelectedType,
            device = currentState.editSelectedDevise,
            montant = montant,
            nomClient = currentState.editNomClient,
            numClient = currentState.editTelephoneClient,
            nomBeneficaire = if (currentState.editSelectedType == TransactionType.DEPOT)
                currentState.editNomBeneficiaire else currentState.editNomBeneficiaire.takeIf { it.isNotBlank() },
            numBeneficaire = if (currentState.editSelectedType == TransactionType.DEPOT)
                currentState.editTelephoneBeneficiaire else currentState.editTelephoneBeneficiaire.takeIf { it.isNotBlank() },
            note = currentState.editNote
        )

        viewModelScope.launch {
            updateTransactionUseCase(updatedTransaction).collect { result ->
                when (result) {
                    is Result.Loading -> {
                        _uiState.update {
                            it.copy(isLoading = true, editErrorMessage = "", errorMessage = "")
                        }
                    }

                    is Result.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                isEditSheetVisible = false,
                                selectedTransaction = null,
                                editErrorMessage = "",
                                isEditMontantError = false,
                                isEditNomClientError = false,
                                isEditTelephoneClientError = false,
                                isEditNomBeneficiaireError = false,
                                isEditTelephoneBeneficiaireError = false
                            )
                        }
                        _uiEvent.emit(RapportUiEvent.ShowSuccesMessage("Mise à jour effectuée"))
                        gettransactionByOperateurAndDevise()
                    }

                    is Result.Error -> {
                        when (val error = result.e) {
                            is TransactionValidationException.FieldRequired -> {
                                _uiState.update {
                                    when (error.field) {
                                        TransactionField.NOM_CLIENT -> it.copy(
                                            isEditNomClientError = true,
                                            isLoading = false
                                        )

                                        TransactionField.TEL_CLIENT -> it.copy(
                                            isEditTelephoneClientError = true,
                                            isLoading = false
                                        )

                                        TransactionField.NOM_BENEF -> it.copy(
                                            isEditNomBeneficiaireError = true,
                                            isLoading = false
                                        )

                                        TransactionField.TEL_BENEF -> it.copy(
                                            isEditTelephoneBeneficiaireError = true,
                                            isLoading = false
                                        )

                                        else -> it.copy(isLoading = false)
                                    }
                                }
                            }

                            is TransactionValidationException.InvalidAmount -> {
                                _uiState.update {
                                    it.copy(
                                        isLoading = false,
                                        isEditMontantError = true
                                    )
                                }
                            }

                            else -> {
                                _uiState.update {
                                    it.copy(
                                        isLoading = false,
                                        editErrorMessage = error?.message
                                            ?: "Erreur lors de la mise à jour"
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    init {
        gettransactionByOperateurAndDevise()
    }

    fun gettransactionByOperateurAndDevise() {


        viewModelScope.launch {

            getTransactionByOperateurAndDeviseUseCase(
                _selectedOperateur.id,
                _selectedDevise
            ).collect { result ->


                when (result) {

                    is Result.Loading -> {
                        _uiState.update {
                            it.copy(isLoading = true)
                        }
                    }

                    is Result.Success -> {
                        _cachedTransactions.value = result.data ?: emptyList()
                        filterTransactions()

                        _uiState.update {
                            val currentSelection = it.selectedTransaction
                            val isSelectionStillPresent = currentSelection?.let { selected ->
                                _cachedTransactions.value.any { cached -> cached.id == selected.id }
                            } ?: false
                            it.copy(
                                isLoading = false,
                                selectedTransaction = if (isSelectionStillPresent) currentSelection else null,
                                isTransactionDialogVisible = isSelectionStillPresent && it.isTransactionDialogVisible
                            )
                        }

                    }

                    is Result.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = result.e?.message ?: "Errreur inconu"
                            )
                        }


                    }


                }
            }


        }


    }

    fun onSearchClick() {

        _uiState.update {

            it.copy(isSearchBarShown = true)
        }

    }

    private fun filterTransactions() {
        val query = uiState.value.searchValue.trim().lowercase()
        val selectedOperateurId = _selectedOperateur.id
        val selectedDevise = _selectedDevise

        val filtered = _cachedTransactions.value.filter { transaction ->
            val matchesOperateur = transaction.idOperateur == selectedOperateurId
            val matchesDevise = transaction.device == selectedDevise
            val matchesQuery = if (query.isEmpty()) {
                true
            } else {
                transaction.transactionCode.contains(query, ignoreCase = true) ||
                        transaction.nomClient.orEmpty().contains(query, ignoreCase = true) ||
                        transaction.numClient.orEmpty().contains(query, ignoreCase = true) ||
                        transaction.nomBeneficaire.orEmpty().contains(query, ignoreCase = true) ||
                        transaction.numBeneficaire.orEmpty().contains(query, ignoreCase = true)
            }

            matchesOperateur && matchesDevise && matchesQuery
        }

        _transactionByOperateurAndDevise.value = filtered

        val currentSelection = uiState.value.selectedTransaction
        if (currentSelection != null && filtered.none { it.id == currentSelection.id }) {
            _uiState.update {
                it.copy(
                    selectedTransaction = null,
                    isTransactionDialogVisible = false,
                    isDeleteConfirmationVisible = false
                )
            }
        }
    }

    fun onSearchBarDismiss() {
        _uiState.update {

            it.copy(isSearchBarShown = false)
        }
    }

    fun onSendOneTransactToServer() {

        val transaction = uiState.value.selectedTransaction ?: return

        viewModelScope.launch {
            sendOneTransactToServerUseCase(transaction.toTransaction()).collect { result ->

                when (result) {


                    Result.Loading -> {
                        _uiState.update {
                            it.copy(isLoading = true)
                        }

                    }

                    is Result.Success -> {
                        _uiState.update {
                            it.copy(isLoading = false, isTransactionDialogVisible = false)
                        }

                        _uiEvent.emit(RapportUiEvent.ShowSuccesMessage("Transaction envoyé au serveur"))

                    }
                    is Result.Error -> {
                        _uiState.update {
                            it.copy(isLoading = false, isTransactionDialogVisible = false)
                        }
                        _uiEvent.emit(
                            RapportUiEvent.ShowError(
                                result.e?.message ?: "Erreur inconnu"
                            )
                        )
                    }
                }


            }
        }

    }

}


data class RapportUiState(

    val searchValue: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String = "",
    val selectedOperateur: Operateur = operateurs.first(),
    val selectedDevise: Constants.Devise = Constants.Devise.USD,
    val isSearchBarShown: Boolean = false,
    val selectedTransaction: TransactionEntity? = null,
    val isTransactionDialogVisible: Boolean = false,
    val isDeleteConfirmationVisible: Boolean = false,
    val isEditSheetVisible: Boolean = false,
    val editSelectedType: TransactionType = TransactionType.DEPOT,
    val editSelectedDevise: Constants.Devise = Constants.Devise.USD,
    val editMontant: String = "",
    val editNomClient: String = "",
    val editTelephoneClient: String = "",
    val editNomBeneficiaire: String = "",
    val editTelephoneBeneficiaire: String = "",
    val editNote: String = "",
    val isEditMontantError: Boolean = false,
    val isEditNomClientError: Boolean = false,
    val isEditTelephoneClientError: Boolean = false,
    val isEditNomBeneficiaireError: Boolean = false,
    val isEditTelephoneBeneficiaireError: Boolean = false,
    val editErrorMessage: String = ""

)


sealed class RapportUiEvent {


    data class ShowError(val errorMessage: String) : RapportUiEvent()
    data class ShowSuccesMessage(val succesMessage: String) : RapportUiEvent()


}