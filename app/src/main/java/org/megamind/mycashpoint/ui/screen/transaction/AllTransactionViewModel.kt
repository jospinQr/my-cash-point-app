package org.megamind.mycashpoint.ui.screen.transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.megamind.mycashpoint.data.data_source.local.entity.TransactionEntity
import org.megamind.mycashpoint.domain.model.Operateur
import org.megamind.mycashpoint.domain.model.Transaction
import org.megamind.mycashpoint.domain.model.operateurs
import org.megamind.mycashpoint.domain.repository.TransactionRepository
import org.megamind.mycashpoint.domain.usecase.rapport.GetSyncTransactByOperatorAndDeviseUseCase
import org.megamind.mycashpoint.utils.Constants
import org.megamind.mycashpoint.utils.Result

class AllTransactionViewModel(private val getSyncTransactByOperatorAndDeviseUseCase: GetSyncTransactByOperatorAndDeviseUseCase) :
    ViewModel() {


    private val _uiState = MutableStateFlow(AllTransactUiState())
    val uiState = _uiState.asStateFlow()


    private val _filteredTransactions =
        MutableStateFlow<List<Transaction>>(emptyList())
    val filteredTransactions = _filteredTransactions.asStateFlow()
    private val _cachedTransactions = MutableStateFlow<List<Transaction>>(emptyList())

    init {
        getSyncTransactByOperatorAndDevise()
    }

    private fun getSyncTransactByOperatorAndDevise() {


        val selectedOperateur = _uiState.value.selectedOperateur
        val selectedDevise = _uiState.value.selectedDevise
        viewModelScope.launch {

            getSyncTransactByOperatorAndDeviseUseCase(
                selectedOperateur.id,
                selectedDevise
            ).collect { result ->


                when (result) {
                    is Result.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }

                    is Result.Success -> {

                        if (result.data != null)
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    transactions = result.data
                                )
                            }

                        _cachedTransactions.value = result.data ?: emptyList()
                        filterTransactions()
                    }

                    is Result.Error<*> -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = result.e?.message ?: "Erreur inconnu"
                            )
                        }

                    }
                }

            }

        }

    }

    fun onSelectedOperatorChange(operator: Operateur) {

        _uiState.update {
            it.copy(selectedOperateur = operator)
        }
        getSyncTransactByOperatorAndDevise()


    }

    fun onSelectedDeviseChange(devise: Constants.Devise) {
        _uiState.update {
            it.copy(selectedDevise = devise)
        }

        getSyncTransactByOperatorAndDevise()
    }

    fun onSearchValueChange(value: String) {
        _uiState.update {
            it.copy(searchValue = value)
        }
        filterTransactions()
    }


    fun onSearchBarDismiss() {
        _uiState.update {

            it.copy(isSearchBarShown = false)
        }
    }


    fun onSearchClick() {

        _uiState.update {

            it.copy(isSearchBarShown = true)
        }

    }

    fun onTransactionClick(transaction: Transaction) {
        _uiState.update {
            it.copy(
                selectedTransaction = transaction,
                isTransactionDialogVisible = true,

                )
        }
    }

    fun onTransactionDialogDismiss() {
        _uiState.update {
            it.copy(
                selectedTransaction = null,
                isTransactionDialogVisible = false,

                )
        }
    }


    private fun filterTransactions() {
        val query = uiState.value.searchValue.trim().lowercase()
        val selectedOperateurId = _uiState.value.selectedOperateur.id
        val selectedDevise = _uiState.value.selectedDevise

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
                )
            }
        }
    }

}


data class AllTransactUiState(
    val isLoading: Boolean = false,
    val selectedOperateur: Operateur = operateurs.first(),
    val selectedDevise: Constants.Devise = Constants.Devise.USD,
    val transactions: List<Transaction> = emptyList(),
    val errorMessage: String = "",
    val searchValue: String = "",
    val isSearchBarShown: Boolean = false,
    val selectedTransaction: Transaction? = null,
    val isTransactionDialogVisible: Boolean = false,

    )
