package org.megamind.mycashpoint.ui.screen.rapport

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.megamind.mycashpoint.data.data_source.local.entity.TransactionEntity
import org.megamind.mycashpoint.domain.model.Operateur
import org.megamind.mycashpoint.domain.model.operateurs
import org.megamind.mycashpoint.domain.usecase.rapport.GetTransactionsByOperatorAndDeviceUseCase
import org.megamind.mycashpoint.domain.usecase.transaction.DeleteTransactionUseCase
import org.megamind.mycashpoint.utils.Constants
import org.megamind.mycashpoint.utils.Result

class RapportViewModel(
    private val getTransactionByOperateurAndDeviseUseCase: GetTransactionsByOperatorAndDeviceUseCase,
    private val deleteTransactionUseCase: DeleteTransactionUseCase,

    ) : ViewModel() {


    private val _uiState = MutableStateFlow(RapportUiState())
    val uiState = _uiState.asStateFlow()


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
                isTransactionDialogVisible = true
            )
        }
    }

    fun onTransactionDialogDismiss() {
        _uiState.update {
            it.copy(
                selectedTransaction = null,
                isTransactionDialogVisible = false,
                isDeleteConfirmationVisible = false
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
    val isDeleteConfirmationVisible: Boolean = false

)