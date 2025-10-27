package org.megamind.mycashpoint.ui.screen.main

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class MainViewModel () : ViewModel() {


    private val _uiState = MutableStateFlow(MainUiState())
    val uiState : StateFlow<MainUiState> = _uiState

    fun onIndexChange (index : Int){

        _uiState.update {
            it.copy(
                itemIndex = index
            )
        }

    }




}



data class MainUiState (

    val itemIndex : Int = 0
)