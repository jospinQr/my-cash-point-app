package org.megamind.mycashpoint.domain.usecase.solde

import kotlinx.coroutines.flow.Flow
import org.megamind.mycashpoint.domain.model.Solde
import org.megamind.mycashpoint.domain.repository.SoldeRepository
import org.megamind.mycashpoint.utils.Result

class GetRemoteSoldesByAgenceAndUserUseCase(private val repository: SoldeRepository) {

    operator fun invoke() =
        repository.getSoldeByUserAndAgence()
}


