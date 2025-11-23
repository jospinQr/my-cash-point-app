package org.megamind.mycashpoint.domain.usecase.solde

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.megamind.mycashpoint.domain.model.Solde
import org.megamind.mycashpoint.domain.model.SoldeType
import org.megamind.mycashpoint.domain.repository.SoldeRepository
import org.megamind.mycashpoint.ui.screen.main.utils.Result

class GetSoldeByOperateurEtTypeEtDeviseUseCase(
    private val repository: SoldeRepository
) {
    operator fun invoke(
        idOperateur: Int?,
        devise: String?,
        soldeType: SoldeType?
    ): Flow<Result<Solde?>> = flow {
        if (idOperateur == null || idOperateur <= 0) {
            emit(Result.Error(SoldeValidationException.FieldRequired(SoldeField.OPERATEUR)))
            return@flow
        }
        if (devise.isNullOrBlank()) {
            emit(Result.Error(SoldeValidationException.FieldRequired(SoldeField.DEVISE)))
            return@flow
        }
        if (soldeType == null) {
            emit(Result.Error(SoldeValidationException.FieldRequired(SoldeField.SOLDE_TYPE)))
            return@flow
        }

        repository.getSoldeByOperateurEtTypeEtDevise(idOperateur, devise, soldeType).collect { emit(it) }
    }
}


