package org.megamind.mycashpoint.domain.usecase.commission

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import org.megamind.mycashpoint.domain.model.Commission
import org.megamind.mycashpoint.domain.repository.CommissionRepository
import org.megamind.mycashpoint.utils.Result

class SaveOrUpdateCommissionUseCase(
    private val repository: CommissionRepository
) {
    operator fun invoke(commission: Commission): Flow<Result<Unit>> =
        if (commission.id == 0L) repository.insert(commission) else repository.update(commission)
}




