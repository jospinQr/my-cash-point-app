package org.megamind.mycashpoint.ui.screen.transaction

import android.view.View
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.megamind.mycashpoint.utils.Constants

class TransationViewModel() : ViewModel() {

    private val _uiState = MutableStateFlow(TransactionUiState())
    val uiSate = _uiState.asStateFlow()



    fun onDeviseSelected(devise: Constants.Devise) {

        _uiState.update {
            it.copy(selectedDevise = devise)
        }
    }


}

data class TransactionUiState(
    val selectedDevise: Constants.Devise = Constants.Devise.CDF,

    )

