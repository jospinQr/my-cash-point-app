package org.megamind.mycashpoint.ui.screen.auth.url

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class UrlViewModel() : ViewModel() {

    private val _uiState = MutableStateFlow(UrlUiState())
    val uiState = _uiState.asStateFlow()


    fun onUrlChange(url: String) {
        _uiState.value = _uiState.value.copy(url = url)

    }


}

data class UrlUiState(
    val url: String =""
)