package org.megamind.mycashpoint.domain.usecase.solde

import kotlinx.coroutines.flow.Flow
import org.megamind.mycashpoint.domain.model.Solde
import org.megamind.mycashpoint.domain.model.SoldeType
import org.megamind.mycashpoint.domain.repository.SoldeRepository
import org.megamind.mycashpoint.utils.Result

class GetSoldeFromServerByCreteriaUseCase(private val soldeRepository: SoldeRepository) {


    operator fun invoke(
        codeAgence: String,
        operateurId: Long,
        deviseCode: String,
        soldeType: SoldeType
    ): Flow<Result<Solde>> {


        return soldeRepository.getSoldeFromServerByCriteria(
            codeAgence = codeAgence,
            operateurId = operateurId,
            deviseCode = deviseCode,
            soldeType = soldeType
        )
    }


}