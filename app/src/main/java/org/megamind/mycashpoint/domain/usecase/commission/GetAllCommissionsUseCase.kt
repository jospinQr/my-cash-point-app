package org.megamind.mycashpoint.domain.usecase.commission

import kotlinx.coroutines.flow.Flow
import org.megamind.mycashpoint.domain.model.Commission
import org.megamind.mycashpoint.domain.repository.CommissionRepository
import org.megamind.mycashpoint.utils.Result

class GetAllCommissionsUseCase(
    private val repository: CommissionRepository
) {
    operator fun invoke(): Flow<Result<List<Commission>>> = repository.getAll()
}






