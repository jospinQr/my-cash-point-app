package org.megamind.mycashpoint.domain.usecase.agence

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import org.megamind.mycashpoint.domain.model.Agence
import org.megamind.mycashpoint.domain.repository.AgenceRepository
import org.megamind.mycashpoint.utils.Result

class SaveOrUpdateAgenceUseCase(
    private val repository: AgenceRepository
) {
    operator fun invoke(agence: Agence): Flow<Result<Unit>> {

        if (agence.codeAgence.isBlank()) {
            return flow { emit(Result.Error(AgenceValidationException.FieldRequired(AgenceField.ID))) }
        }
        if (agence.designation.isBlank()) {
            return flow {
                emit(Result.Error(AgenceValidationException.FieldRequired(AgenceField.DESIGNATION)))
            }
        }
        return repository.saveOrUpdate(agence)
    }
}

enum class AgenceField { ID, DESIGNATION }

sealed class AgenceValidationException(message: String) : IllegalArgumentException(message) {
    class FieldRequired(val field: AgenceField) :
        AgenceValidationException("FIELD_REQUIRED:${'$'}field")
}

