package org.megamind.mycashpoint.domain.usecase.solde

import kotlinx.coroutines.flow.Flow
import org.megamind.mycashpoint.domain.model.Solde
import org.megamind.mycashpoint.domain.repository.SoldeRepository
import org.megamind.mycashpoint.utils.Result

class GetSoldeForSyncUsecas(private val repository: SoldeRepository) {

    operator fun invoke(agenceCode: String, lastSyncAt: Long): Flow<Result<List<Solde>>> {

        return repository.getSoldeForSync(agenceCode, lastSyncAt)
    }


}