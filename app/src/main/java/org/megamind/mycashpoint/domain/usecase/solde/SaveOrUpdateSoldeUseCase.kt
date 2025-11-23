package org.megamind.mycashpoint.domain.usecase.solde

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.megamind.mycashpoint.domain.model.Solde
import org.megamind.mycashpoint.domain.repository.SoldeRepository
import org.megamind.mycashpoint.ui.screen.main.utils.Result

class SaveOrUpdateSoldeUseCase(
    private val repository: SoldeRepository
) {
    operator fun invoke(solde: Solde): Flow<Result<Unit>> = flow {
        if (solde.idOperateur <= 0) {
            emit(Result.Error(SoldeValidationException.FieldRequired(SoldeField.OPERATEUR)))
            return@flow
        }
        // Rejeter montant à 0 ou négatif: champ vide côté UI devient 0
        if (solde.montant <= java.math.BigDecimal.ZERO) {
            emit(Result.Error(SoldeValidationException.InvalidAmount))
            return@flow
        }
        // Seuil (si fourni) ne doit pas être négatif
        if (solde.seuilAlerte != null && solde.seuilAlerte < 0) {
            emit(Result.Error(SoldeValidationException.InvalidThreshold))
            return@flow
        }

        repository.insertOrUpdate(solde).collect { emit(it) }
    }
}

enum class SoldeField { OPERATEUR, DEVISE, SOLDE_TYPE }

sealed class SoldeValidationException(message: String) : IllegalArgumentException(message) {
    class FieldRequired(val field: SoldeField) : SoldeValidationException("FIELD_REQUIRED:${'$'}field")
    data object InvalidAmount : SoldeValidationException("INVALID_AMOUNT")
    data object InvalidThreshold : SoldeValidationException("INVALID_THRESHOLD")
}


