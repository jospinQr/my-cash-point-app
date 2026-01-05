package org.megamind.mycashpoint.ui.screen.admin.caisse_mouvement

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import org.megamind.mycashpoint.domain.model.Agence
import org.megamind.mycashpoint.domain.model.Operateur
import org.megamind.mycashpoint.domain.model.SoldeMouvement
import org.megamind.mycashpoint.domain.model.operateurs
import org.megamind.mycashpoint.domain.usecase.solde.GetSoldeMouvementUseCase
import org.megamind.mycashpoint.utils.Result
import org.megamind.mycashpoint.utils.toLocalDate
import org.threeten.bp.LocalDate
import org.threeten.bp.temporal.WeekFields
import java.util.Locale

class CaisseMouvementViewModel(private val getSoldeMouvementUseCase: GetSoldeMouvementUseCase) :
    ViewModel() {

    private val _uiState = MutableStateFlow(AdminTransactionUiState())
    val uiState = _uiState.asStateFlow()


    init {
        getSoldeMouvement()
    }


    private fun getSoldeMouvement() {


        getSoldeMouvementUseCase(
            codeAgence = "AG001",
            page = 0,
            size = 100,
        ).onEach { result ->


            _uiState.update { state ->

                when (result) {
                    is Result.Loading -> {
                        state.copy(isTransactionLoading = true)

                    }

                    is Result.Success -> {
                        val filtres = filtrerMouvements(
                            mouvements = result.data!!, periode = state.periodeFiltre
                        )
                        state.copy(
                            isTransactionLoading = false,
                            mouvements = result.data,
                            mouvementsFiltres = filtres
                        )
                    }

                    is Result.Error -> {
                        state.copy(
                            isTransactionLoading = false, transactionError = result.e?.message
                        )

                    }

                }

            }

        }.launchIn(viewModelScope)


    }

    fun onSelectedOperateurChange(operateur: Operateur) {
        _uiState.value = _uiState.value.copy(selectedOperateur = operateur)
    }

    fun onSelectedAgenceChange(agence: Agence) {
        _uiState.value = _uiState.value.copy(selectedAgence = agence)
    }


    private fun filtrerMouvements(
        mouvements: List<SoldeMouvement>, periode: PeriodeFiltre
    ): List<SoldeMouvement> {

        val today = LocalDate.now()
        val weekFields = WeekFields.of(Locale.getDefault())
        val currentWeek = today.get(weekFields.weekOfWeekBasedYear())
        val currentYear = today.year

        return mouvements.filter { mouvement ->
            val date = mouvement.dateMouvement.toLocalDate()

            when (periode) {

                PeriodeFiltre.AUJOURDHUI -> date.isEqual(today)

                PeriodeFiltre.SEMAINE -> date.get(weekFields.weekOfWeekBasedYear()) == currentWeek && date.year == currentYear

                PeriodeFiltre.MOIS -> date.month == today.month && date.year == today.year

                PeriodeFiltre.ANNEE -> date.year == today.year
            }
        }
    }

    fun onPeriodeChange(periode: PeriodeFiltre) {
        _uiState.update { state ->
            state.copy(
                periodeFiltre = periode, mouvementsFiltres = filtrerMouvements(
                    state.mouvements!!, periode
                )
            )
        }
    }


}


data class AdminTransactionUiState(
    val isTransactionLoading: Boolean = false,
    val mouvements: List<SoldeMouvement>? = null,
    val transactionError: String? = null,
    val selectedOperateur: Operateur = operateurs.first(),
    val selectedAgence: Agence = Agence(),
    val mouvementsFiltres: List<SoldeMouvement>? = null,
    val periodeFiltre: PeriodeFiltre = PeriodeFiltre.AUJOURDHUI
)

enum class PeriodeFiltre {
    AUJOURDHUI, SEMAINE, MOIS, ANNEE
}

