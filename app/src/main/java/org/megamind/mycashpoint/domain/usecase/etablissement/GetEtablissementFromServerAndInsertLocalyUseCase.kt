package org.megamind.mycashpoint.domain.usecase.etablissement

import kotlinx.coroutines.flow.Flow
import org.megamind.mycashpoint.domain.repository.EtablissementRepository
import org.megamind.mycashpoint.utils.Result

class GetEtablissementFromServerAndInsertLocalyUseCase(private val repository: EtablissementRepository) {
    operator fun invoke(): Flow<Result<Unit>> {
        return repository.getFromServerAndInsertLocaly()
    }
}
