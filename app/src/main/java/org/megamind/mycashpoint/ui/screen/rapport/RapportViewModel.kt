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
import org.megamind.mycashpoint.domain.model.Etablissement
import org.megamind.mycashpoint.domain.model.Operateur
import org.megamind.mycashpoint.domain.model.Transaction
import org.megamind.mycashpoint.domain.model.TransactionType
import org.megamind.mycashpoint.domain.model.operateurs
import org.megamind.mycashpoint.domain.usecase.etablissement.GetEtablissementFromLocalUseCase
import org.megamind.mycashpoint.domain.usecase.rapport.GetNonSyncTransactByOperatorAndDeviseUseCase
import org.megamind.mycashpoint.domain.usecase.solde.SyncSoldesUseCase
import org.megamind.mycashpoint.domain.usecase.transaction.DeleteTransactionUseCase
import org.megamind.mycashpoint.domain.usecase.transaction.SendOneTransactToServerUseCase
import org.megamind.mycashpoint.domain.usecase.transaction.SyncTransactionUseCase
import org.megamind.mycashpoint.domain.usecase.transaction.UpdateTransactionUseCase
import org.megamind.mycashpoint.utils.Constants
import org.megamind.mycashpoint.utils.Result
import java.math.BigDecimal

class RapportViewModel(
    private val getNonSyncTransactByOperatorAndDeviseUseCase: GetNonSyncTransactByOperatorAndDeviseUseCase,
    private val deleteTransactionUseCase: DeleteTransactionUseCase,
    private val updateTransactionUseCase: UpdateTransactionUseCase,
    private val sendOneTransactToServerUseCase: SendOneTransactToServerUseCase,
    private val syncTransactionUseCase: SyncTransactionUseCase,
    private val syncSoldesUseCase: SyncSoldesUseCase,
    private val getEtablissementFromLocalUseCase: GetEtablissementFromLocalUseCase

) : ViewModel() {


    private val _uiState = MutableStateFlow(RapportUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<RapportUiEvent>()
    val uiEvent: SharedFlow<RapportUiEvent> = _uiEvent.asSharedFlow()

    private val _cachedTransactions = MutableStateFlow<List<Transaction>>(emptyList())
    private val _filteredTransactions =
        MutableStateFlow<List<Transaction>>(emptyList())
    val filteredTransactions = _filteredTransactions.asStateFlow()

    private val _selectedDevise get() = uiState.value.selectedDevise
    private val _selectedOperateur get() = uiState.value.selectedOperateur


    init {
        gettransactionByOperateurAndDevise()
        getEtablissement()
    }

    private fun getEtablissement() {
        viewModelScope.launch {
            getEtablissementFromLocalUseCase().collect { result ->
                if (result is Result.Success) {
                    _uiState.update { it.copy(etablissement = result.data) }
                }
            }
        }
    }

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

    fun onTransactionClick(transaction: Transaction) {
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

    fun onDeleteTransactionRequest(transaction: Transaction) {
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

    fun onEditTransactionRequest(transaction: Transaction) {
        _uiState.update {
            it.copy(
                selectedTransaction = transaction,
                isTransactionDialogVisible = false,
                isDeleteConfirmationVisible = false,
                isEditSheetVisible = true,
                editSelectedType = transaction.type,
                editSelectedDevise = transaction.devise,
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

        val updatedTransaction = transaction.copy(
            type = currentState.editSelectedType,
            devise = currentState.editSelectedDevise,
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

                        _uiState.update {
                            it.copy(isLoading = false, editErrorMessage = result.e?.message ?: "")
                        }
                    }
                }
            }
        }
    }


    fun gettransactionByOperateurAndDevise() {


        viewModelScope.launch {

            getNonSyncTransactByOperatorAndDeviseUseCase(
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
            val matchesDevise = transaction.devise == selectedDevise
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

        _filteredTransactions.value = filtered

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
            sendOneTransactToServerUseCase(transaction).collect { result ->

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
                        gettransactionByOperateurAndDevise()
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

    fun onSyncTransaction() {

        viewModelScope.launch {

            syncTransactionUseCase().collect { result ->

                when (result) {

                    is Result.Loading -> {
                        _uiState.update {
                            it.copy(isLoading = true)
                        }
                    }

                    is Result.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                isSyncTransactConformDialogShown = false,
                                isActionMenuVisible = false
                            )
                        }

                        _uiEvent.emit(RapportUiEvent.ShowSuccesMessage("Synchronisation reussit"))
                        gettransactionByOperateurAndDevise()
                    }

                    is Result.Error -> {
                        _uiState.update {
                            it.copy(isLoading = false, isSyncTransactConformDialogShown = false)
                        }

                        _uiEvent.emit(
                            RapportUiEvent.ShowError(
                                result.e?.message ?: "Erreur inconnue"
                            )
                        )
                    }
                }


            }

        }

    }


    fun onSyncSolde() {
        viewModelScope.launch {

            syncSoldesUseCase().collect { result ->
                when (result) {
                    Result.Loading -> {
                        _uiState.update {
                            it.copy(isLoading = true)
                        }
                    }

                    is Result.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                isSyncSoldeConformDialogShown = false,
                                isActionMenuVisible = false
                            )
                        }

                        _uiEvent.emit(RapportUiEvent.ShowSuccesMessage("Solde mis à jour avec succès"))
                    }

                    is Result.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                isSyncSoldeConformDialogShown = false,
                                errorMessage = result.e?.message ?: "Erreur inconnue"
                            )
                        }

                        _uiEvent.emit(
                            RapportUiEvent.ShowError(
                                result.e?.message ?: "Erreur inconnue"
                            )
                        )

                    }
                }

            }


        }

    }


    fun onActionMenuVisibile() {

        _uiState.update {
            it.copy(isActionMenuVisible = true)
        }

    }

    fun onActionMenuDismiss() {
        _uiState.update {
            it.copy(isActionMenuVisible = false)
        }

    }

    fun onSyncTransactConfirmDialog() {

        _uiState.update {
            it.copy(isSyncTransactConformDialogShown = true)
        }
    }

    fun onSyncTransactConfirmDialogDismiss() {

        _uiState.update {
            it.copy(isSyncTransactConformDialogShown = false)
        }
    }

    fun onSyncSoldeConfirmDialog() {

        _uiState.update {
            it.copy(isSyncSoldeConformDialogShown = true)
        }
    }

    fun onSyncSoldeConfirmDialogDismiss() {

        _uiState.update {
            it.copy(isSyncSoldeConformDialogShown = false)
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
    val selectedTransaction: Transaction? = null,
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
    val editErrorMessage: String = "",
    val isActionMenuVisible: Boolean = false,
    val isSyncTransactConformDialogShown: Boolean = false,
    val isSyncSoldeConformDialogShown: Boolean = false,
    val etablissement: Etablissement? = null

) {

}

sealed class RapportUiEvent {


    data class ShowError(val errorMessage: String) : RapportUiEvent()
    data class ShowSuccesMessage(val succesMessage: String) : RapportUiEvent()


}

sealed class DialogState {
    data object None : DialogState()
    data class TransactionDetail(val transaction: Transaction) : DialogState()
    data class DeleteConfirmation(val transaction: Transaction) : DialogState()
    data object Loading : DialogState()
}