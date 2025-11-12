package org.megamind.mycashpoint.domain.usecase.commission

import kotlinx.coroutines.flow.Flow
import org.megamind.mycashpoint.domain.model.Commission
import org.megamind.mycashpoint.domain.repository.CommissionRepository
import org.megamind.mycashpoint.utils.Result

class InsertAllCommissionsUseCase(
    private val repository: CommissionRepository
) {
    operator fun invoke(commissions: List<Commission>): Flow<Result<Unit>> =
        repository.insertAll(commissions)
}






