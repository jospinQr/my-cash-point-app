package org.megamind.mycashpoint.ui.screen.operateur

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.megamind.mycashpoint.domain.model.Operateur

class OperateurViewModel : ViewModel() {


    private val _uiSate = MutableStateFlow(OperateurUiState())
    val uiState = _uiSate.asStateFlow()

    fun onOperateurSelected(operateur: Operateur) {
        _uiSate.update {
            it.copy(selectedOperateur = operateur)
        }

    }
}

data class OperateurUiState(
    val selectedOperateur: Operateur? = null
)