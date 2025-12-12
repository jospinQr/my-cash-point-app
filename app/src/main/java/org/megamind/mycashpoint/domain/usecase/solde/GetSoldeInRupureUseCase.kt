package org.megamind.mycashpoint.domain.usecase.solde

import kotlinx.coroutines.flow.Flow
import org.megamind.mycashpoint.domain.model.Solde
import org.megamind.mycashpoint.domain.repository.SoldeRepository
import org.megamind.mycashpoint.utils.Result

class GetSoldeInRutureUseCase(private val soldeRepository: SoldeRepository) {


    operator fun invoke(): Flow<Result<List<Solde>>> = soldeRepository.getSoldeInRupture()

}

