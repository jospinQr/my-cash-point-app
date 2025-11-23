package org.megamind.mycashpoint.domain.usecase.agence

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.megamind.mycashpoint.domain.model.Agence
import org.megamind.mycashpoint.domain.repository.AgenceRepository
import org.megamind.mycashpoint.ui.screen.main.utils.Result

class SaveOrUpdateAgenceUseCase(
    private val repository: AgenceRepository
) {
    operator fun invoke(agence: Agence): Flow<Result<Unit>> = flow {
        // Validation métier minimale
        if (agence.codeAgence.isBlank()) {
            emit(Result.Error(AgenceValidationException.FieldRequired(AgenceField.ID)))
            return@flow
        }
        if (agence.designation.isBlank()) {
            emit(Result.Error(AgenceValidationException.FieldRequired(AgenceField.DESIGNATION)))
            return@flow
        }

        // Délégation au repository si la validation passe
        repository.saveOrUpdate(agence).collect { emit(it) }
    }
}

enum class AgenceField { ID, DESIGNATION }

sealed class AgenceValidationException(message: String) : IllegalArgumentException(message) {
    class FieldRequired(val field: AgenceField) : AgenceValidationException("FIELD_REQUIRED:${'$'}field")
}

