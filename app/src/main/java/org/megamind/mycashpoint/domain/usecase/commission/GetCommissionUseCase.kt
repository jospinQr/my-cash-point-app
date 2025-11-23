package org.megamind.mycashpoint.domain.usecase.commission

import kotlinx.coroutines.flow.Flow
import org.megamind.mycashpoint.domain.model.Commission
import org.megamind.mycashpoint.domain.model.TransactionType
import org.megamind.mycashpoint.domain.repository.CommissionRepository
import org.megamind.mycashpoint.ui.screen.main.utils.Constants
import org.megamind.mycashpoint.ui.screen.main.utils.Result

class GetCommissionUseCase(
    private val repository: CommissionRepository
) {
    operator fun invoke(
        idOperateur: Int,
        type: TransactionType,
        devise: Constants.Devise
    ): Flow<Result<Commission?>> = repository.getCommission(idOperateur, type, devise)
}










