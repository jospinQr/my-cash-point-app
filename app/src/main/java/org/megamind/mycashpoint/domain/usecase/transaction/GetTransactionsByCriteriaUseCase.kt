package org.megamind.mycashpoint.domain.usecase.transaction

import kotlinx.coroutines.flow.Flow
import org.megamind.mycashpoint.domain.model.PaginatedTransaction
import org.megamind.mycashpoint.domain.model.TransactionType
import org.megamind.mycashpoint.domain.repository.TransactionRepository
import org.megamind.mycashpoint.utils.Result

class GetTransactionsByCriteriaUseCase(private val repository: TransactionRepository) {

    operator fun invoke(
        codeAgence: String,
        operateurId: Long,
        deviseCode: String,
        type: TransactionType,
        page: Int,
        size: Int
    ): Flow<Result<PaginatedTransaction>> {
        return repository.getFromServerByCriteria(
            codeAgence = codeAgence,
            operateurId = operateurId,
            deviseCode = deviseCode,
            type = type,
            page = page,
            size = size
        )
    }
}
