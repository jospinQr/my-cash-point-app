package org.megamind.mycashpoint.ui.screen.admin.dash_board

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import org.megamind.mycashpoint.domain.model.Agence
import org.megamind.mycashpoint.domain.model.Analytics
import org.megamind.mycashpoint.domain.model.Operateur
import org.megamind.mycashpoint.domain.model.Solde
import org.megamind.mycashpoint.domain.model.SoldeType
import org.megamind.mycashpoint.domain.model.TopOperateur
import org.megamind.mycashpoint.domain.model.operateurs
import org.megamind.mycashpoint.domain.usecase.agence.GetAgencesUseCase
import org.megamind.mycashpoint.domain.usecase.analytics.GetAgenceAnalyticsUseCase
import org.megamind.mycashpoint.domain.usecase.solde.AdminSaveSoldeUseCase
import org.megamind.mycashpoint.domain.usecase.solde.GetSoldeFromServerByCreteriaUseCase
import org.megamind.mycashpoint.domain.usecase.solde.GetSoldeInRutureUseCase
import org.megamind.mycashpoint.domain.usecase.solde.SaveOrUpdateSoldeUseCase
import org.megamind.mycashpoint.domain.usecase.solde.SoldeValidationException
import org.megamind.mycashpoint.domain.usecase.transaction.GetTopOperateurUseCase
import org.megamind.mycashpoint.utils.Constants
import org.megamind.mycashpoint.utils.DataStorageManager
import org.megamind.mycashpoint.utils.Result
import org.megamind.mycashpoint.utils.decodeJwtPayload
import org.megamind.mycashpoint.utils.toBigDecimalOrNull
import java.math.BigDecimal

class DashBoardViewModel(
    private val getAllAgenceUseCase: GetAgencesUseCase,
    private val getSoldeByCriteriaUseCase: GetSoldeFromServerByCreteriaUseCase,
    private val getTopOperateurUseCase: GetTopOperateurUseCase,
    private val getSoldeInRuptureUseCase: GetSoldeInRutureUseCase,
    private val dataStorageManager: DataStorageManager,
    private val saveOrUpdateSolde: AdminSaveSoldeUseCase,
    private val analyticsUseCase: GetAgenceAnalyticsUseCase
) : ViewModel() {


    private val TAG = "DashBoardViewModel"
    private val _uiState = MutableStateFlow(DashBoardUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<DashBoardUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()


    private var hasLoadedAgences = false


    init {
        getAllAgence()
    }

    fun getAllAgence() {

        viewModelScope.launch {
            getAllAgenceUseCase().collect { result ->
                when (result) {

                    is Result.Loading -> {
                        _uiState.update {
                            it.copy(isAgenceLoading = true)
                        }
                        Log.i(TAG, "Agence Loading")

                    }

                    is Result.Success -> {

                        hasLoadedAgences = true
                        result.data?.let {
                            _uiState.update {
                                it.copy(
                                    isAgenceLoading = false,
                                    agenceList = result.data,
                                    agenceErrorMessage = null,
                                    selectedAgence = result.data.firstOrNull()
                                )
                            }
                            getSoldeByCriteria()
                            getTopOperateur()

                        }


                        Log.i(TAG, "Agence : ${result.data}")

                    }

                    is Result.Error -> {
                        _uiState.update {
                            it.copy(
                                agenceErrorMessage = result.e?.message ?: "",
                                isAgenceLoading = false
                            )
                        }



                        Log.i(TAG, "Agence Error : ${result.e?.message}")

                    }


                }
            }

        }
    }

    fun getSoldeByCriteria() {
        val agence = _uiState.value.selectedAgence ?: return
        val operateur = _uiState.value.selectedOperateur ?: return
        val devise = _uiState.value.selectedDevise
        val type = _uiState.value.selectedSoldeType

        viewModelScope.launch {
            getSoldeByCriteriaUseCase(
                codeAgence = agence.codeAgence,
                operateurId = operateur.id.toLong(),
                deviseCode = devise.name,
                soldeType = type
            )
                .collect { result ->

                    when (result) {

                        is Result.Loading -> {
                            _uiState.update {
                                it.copy(
                                    isSoldeLoading = true,
                                )
                            }
                        }

                        is Result.Success -> {
                            _uiState.update {
                                it.copy(
                                    isSoldeLoading = false,
                                    soldeErrorMessage = null,
                                    currenteSolde = result.data

                                )
                            }
                        }

                        is Result.Error -> {
                            _uiState.update {
                                it.copy(
                                    isSoldeLoading = false,
                                    soldeErrorMessage = result.e?.message ?: "Erreur inconnue"
                                )
                            }
                        }


                    }
                }
        }
    }

    fun getTopOperateur() {
        val agence = _uiState.value.selectedAgence ?: return
        val devise = _uiState.value.selectedDevise

        viewModelScope.launch {
            getTopOperateurUseCase(
                codeAgence = agence.codeAgence,
                devise = devise
            ).collect { result ->

                when (result) {
                    is Result.Loading -> {


                        _uiState.update {
                            it.copy(
                                isTopOperateurLoading = true,
                            )
                        }

                    }

                    is Result.Success -> {
                        _uiState.update {
                            it.copy(
                                isTopOperateurLoading = false,
                                topOperateur = result.data ?: emptyList()
                            )
                        }

                    }

                    is Result.Error -> {
                        _uiState.update {
                            it.copy(
                                isTopOperateurLoading = false,
                                topOperateurErrorMessage = result.e?.message ?: "Erreur inconnue"
                            )
                        }
                    }
                }


            }
        }

    }

    fun getSoldeInRupture() {
        viewModelScope.launch {

            getSoldeInRuptureUseCase().collect { result ->
                when (result) {
                    is Result.Loading -> {
                        _uiState.update {
                            it.copy(isSoldeInRuptureLoading = true)
                        }
                    }

                    is Result.Success -> {
                        _uiState.update {
                            it.copy(
                                isSoldeInRuptureLoading = false,
                                soldeInRupture = result.data ?: emptyList()
                            )
                        }
                    }

                    is Result.Error -> {
                        _uiState.update {
                            it.copy(
                                isSoldeInRuptureLoading = false,
                                soldeInRuptureErrorMessage = result.e?.message ?: "Erreur inconnue"
                            )
                        }
                    }
                }

            }

        }
    }

    fun onSelectedAgence(agence: Agence) {
        _uiState.value =
            _uiState.value.copy(selectedAgence = agence, isAgenceDropDownExpanded = false)
        getSoldeByCriteria()
        getTopOperateur()

    }

    fun onSelectedDeviseChange(devise: Constants.Devise) {
        _uiState.update { it.copy(selectedDevise = devise) }
        getSoldeByCriteria()
        getTopOperateur()


    }


    fun onAgenceDropdownExpanded(expanded: Boolean) {
        _uiState.update { it.copy(isAgenceDropDownExpanded = expanded) }
    }

    fun onSelectedOperateurChange(operateur: Operateur) {
        _uiState.update { it.copy(selectedOperateur = operateur) }
        getSoldeByCriteria()
    }

    fun onSelectedSoldeTypeChange(soldeType: SoldeType) {
        _uiState.update { it.copy(selectedSoldeType = soldeType) }
        getSoldeByCriteria()
    }

    fun onSoldeInRuptureDialogShown() {
        viewModelScope.launch {
            _uiEvent.emit(DashBoardUiEvent.NavigateToEtablissement)
        }
    }

    fun onSoldeInRuptureDialogDismiss() {
        _uiState.update { it.copy(isSoldeInRuptureDialogShown = false) }
    }

    fun onInitSoldeClick() {
        _uiState.update { it.copy(isInitSoldeBottomSheetShown = true) }

    }

    fun onInitSoldeBottomDismiss() {
        _uiState.update {
            it.copy(isInitSoldeBottomSheetShown = false)
        }
    }

    fun onSaveClick() {


        viewModelScope.launch {

            val claims = decodeJwtPayload(dataStorageManager.getToken()!!)
            val solde = Solde(
                idOperateur = uiState.value.initSelectedOperateur.id,
                montant = uiState.value.solde.toBigDecimalOrNull() ?: BigDecimal(0),
                devise = uiState.value.initSelectedDevise,
                seuilAlerte = uiState.value.seuilAlert?.toDouble(),
                dernierMiseAJour = System.currentTimeMillis(),
                misAJourPar = claims.optString("sub").toLong(),
                soldeType = uiState.value.initSoldeType,
                agenceCode = uiState.value.selectedAgence?.codeAgence ?: ""

            )
            saveOrUpdateSolde(solde).collect { result ->

                when (result) {

                    Result.Loading -> {
                        _uiState.update {
                            it.copy(isSoldeSaveLoading = true)
                        }
                    }

                    is Result.Success -> {
                        _uiState.update {
                            it.copy(
                                isSoldeSaveLoading = false,
                                isInitSoldeBottomSheetShown = false,
                                solde = "",
                                seuilAlert = null,
                                isSoldeError = false,
                                isSeuilError = false,
                            )
                        }
                        _uiEvent.emit(DashBoardUiEvent.ShowSuccesMessage("Solde enregistré avec succès"))

                    }

                    is Result.Error<*> -> {
                        _uiState.update {
                            it.copy(
                                isSoldeSaveLoading = false,
                                isInitSoldeBottomSheetShown = false
                            )
                        }
                        when (result.e) {
                            is SoldeValidationException.InvalidAmount -> {
                                _uiState.update { it.copy(isSoldeError = true) }
                            }

                            is SoldeValidationException.InvalidThreshold -> {
                                _uiState.update { it.copy(isSeuilError = true) }
                            }

                            else -> {
                                _uiEvent.emit(
                                    DashBoardUiEvent.ShowError(
                                        result.e?.message ?: "Erreur inconnue"
                                    )
                                )
                            }
                        }
                    }


                }
            }
        }

    }


    fun onConfirmDialogShown() {

        val soldeAmount = uiState.value.solde.toBigDecimalOrNull()

        Log.d(
            TAG,
            "onConfirmDialogShown - solde: ${uiState.value.solde}, soldeAmount: $soldeAmount"
        )

        if (uiState.value.solde.isEmpty() || soldeAmount == null || soldeAmount <= BigDecimal.ZERO) {
            Log.d(TAG, "Validation failed - showing error")
            _uiState.update {
                it.copy(isSoldeError = true)
            }
            return
        }

        Log.d(TAG, "Validation passed - showing dialog")
        _uiState.update {
            it.copy(isConfirmDialogShown = true)
        }

    }

    fun onConfirmDialogDismiss() {
        _uiState.update {
            it.copy(isConfirmDialogShown = false)

        }
    }

    fun onSeuilChange(seuil: String) {
        _uiState.update {
            it.copy(seuilAlert = seuil, isSeuilError = false)
        }
    }

    fun onSoldeChange(solde: String) {
        Log.d(TAG, "onSoldeChange - new value: $solde")
        _uiState.update {
            it.copy(solde = solde, isSoldeError = false)
        }
    }

    fun onInitSoldeTypeChange(soldeType: SoldeType) {
        _uiState.update {
            it.copy(initSoldeType = soldeType)
        }
    }

    fun onInitSelectedOperateurChange(operateur: Operateur) {
        _uiState.update {
            it.copy(initSelectedOperateur = operateur)
        }
    }

    fun onInitSelectedDeviseChange(devise: Constants.Devise) {
        _uiState.update {
            it.copy(initSelectedDevise = devise)
        }
    }

    fun onInitOperateurMenuExpanded() {
        _uiState.update {
            it.copy(isInitOperateurExpanded = true)
        }
    }

    fun onInitOperateurMenuDismiss() {
        _uiState.update {
            it.copy(isInitOperateurExpanded = false)
        }
    }


    fun onLogOut() {
        viewModelScope.launch(Dispatchers.IO) {
            dataStorageManager.saveToken("")
            _uiEvent.emit(DashBoardUiEvent.NavigateToLogin)

        }

    }


    fun getAnalytics() {

        viewModelScope.launch {
            analyticsUseCase(_uiState.value.selectedAgence?.codeAgence!!).collect { result ->

                when (result) {
                    is Result.Loading -> {
                        _uiState.update {
                            it.copy(isAnalyticsLoading = true)
                        }

                    }

                    is Result.Success -> {
                        _uiState.update {
                            it.copy(
                                currentAgenceAnalytics = result.data,
                                isAnalyticsLoading = false
                            )
                        }

                    }

                    is Result.Error -> {
                        _uiState.update {
                            it.copy(
                                analyticsErrorMessage = result.e?.message ?: "Erreur inconnue",
                                isAnalyticsLoading = false
                            )

                        }
                    }

                }
            }
        }


    }


    fun onAnalyticsDialogShown() {
        getAnalytics()
        _uiState.update {
            it.copy(isAnalyticsDialogShown = true)
        }
    }


    fun onAnalyticsDialogDismiss() {
        _uiState.update {
            it.copy(isAnalyticsDialogShown = false)
        }
    }

}


data class DashBoardUiState(
    val agenceList: List<Agence> = emptyList(),
    val selectedAgence: Agence? = null,

    val isAgenceLoading: Boolean = false,
    val isSoldeLoading: Boolean = false,
    val isTopOperateurLoading: Boolean = false,
    val isSoldeInRuptureLoading: Boolean = false,


    val agenceErrorMessage: String? = null,
    val soldeErrorMessage: String? = null,
    val topOperateurErrorMessage: String? = null,
    val soldeInRuptureErrorMessage: String? = null,

    val isAgenceDropDownExpanded: Boolean = false,

    val selectedOperateur: Operateur = operateurs.first(),
    val selectedDevise: Constants.Devise = Constants.Devise.USD,
    val selectedSoldeType: SoldeType = SoldeType.PHYSIQUE,
    val currenteSolde: Solde? = null,
    val topOperateur: List<TopOperateur> = emptyList(),
    val soldeInRupture: List<Solde> = emptyList(),
    val isSoldeInRuptureDialogShown: Boolean = false,

    val isInitSoldeBottomSheetShown: Boolean = false,
    val solde: String = "",
    val initSoldeType: SoldeType = SoldeType.PHYSIQUE,
    val initSelectedOperateur: Operateur = operateurs.first(),
    val initSelectedDevise: Constants.Devise = Constants.Devise.USD,
    val seuilAlert: String? = null,
    val isSoldeError: Boolean = false,
    val isSeuilError: Boolean = false,
    val isInitOperateurExpanded: Boolean = false,
    val isConfirmDialogShown: Boolean = false,

    val isSoldeSaveLoading: Boolean = false,

    val isAnalyticsDialogShown: Boolean = false,
    val currentAgenceAnalytics: Analytics? = null,
    val isAnalyticsLoading: Boolean = false,
    val analyticsErrorMessage: String? = null


)


sealed class DashBoardUiEvent {


    object NavigateToLogin : DashBoardUiEvent()

    data class ShowSuccesMessage(val successMessage: String) : DashBoardUiEvent()

    data class ShowError(val errorMessage: String) : DashBoardUiEvent()

    object NavigateToEtablissement : DashBoardUiEvent()


}
