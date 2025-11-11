package org.megamind.mycashpoint.domain.usecase.commission

import kotlinx.coroutines.flow.Flow
import org.megamind.mycashpoint.domain.model.CommissionStats
import org.megamind.mycashpoint.domain.repository.CommissionRepository
import org.megamind.mycashpoint.utils.Result

class GetCommissionStatsParDeviseUseCase(
    private val repository: CommissionRepository
) {
    operator fun invoke(): Flow<Result<List<CommissionStats>>> = repository.getStatsParDevise()
}




