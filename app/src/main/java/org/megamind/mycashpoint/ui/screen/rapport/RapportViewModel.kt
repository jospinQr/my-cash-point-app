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
import org.megamind.mycashpoint.utils.Constants
import org.megamind.mycashpoint.utils.Result

class RapportViewModel(
    private val getTransactionByOperateurAndDeviseUseCase: GetTransactionsByOperatorAndDeviceUseCase,

    ) : ViewModel() {




    private val _uiState = MutableStateFlow(RapportUiState())
    val uiState = _uiState.asStateFlow()


    private val _transactionByOperateurAndDevise =
        MutableStateFlow<List<TransactionEntity>>(emptyList())
    val transaction = _transactionByOperateurAndDevise.asStateFlow()

    private val _selectedDevise get() = uiState.value.selectedDevise
    private val _selectedOperateur get() = uiState.value.selectedOperateur

    fun onSelectedOperateurChange(operateur: Operateur) {

        _uiState.update {
            it.copy(selectedOperateur = operateur)
        }
        gettransactionByOperateurAndDevise()

    }

    fun onSelectedDeviseChange(devise: Constants.Devise) {
        _uiState.update {
            it.copy(selectedDevise = devise)
        }

        gettransactionByOperateurAndDevise()
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
                        _uiState.update {
                            it.copy(isLoading = false)
                        }

                        _transactionByOperateurAndDevise.value = result.data ?: emptyList()

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


}


data class RapportUiState(

    val isLoading: Boolean = false,
    val errorMessage: String = "",
    val selectedOperateur: Operateur = operateurs.first(),
    val selectedDevise: Constants.Devise = Constants.Devise.USD,

    )