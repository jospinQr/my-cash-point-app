package org.megamind.mycashpoint.domain.usecase.etablissement

import kotlinx.coroutines.flow.Flow
import org.megamind.mycashpoint.domain.model.Etablissement
import org.megamind.mycashpoint.domain.repository.EtablissementRepository
import org.megamind.mycashpoint.utils.Result

class UpdateEtablissementUseCase(private val repository: EtablissementRepository) {
    operator fun invoke(etablissement: Etablissement): Flow<Result<Unit>> {
        return repository.editEtablissement(etablissement)
    }
}
