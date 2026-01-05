package org.megamind.mycashpoint.domain.usecase.transaction

import kotlinx.coroutines.flow.Flow
import org.megamind.mycashpoint.domain.model.PaginatedTransaction
import org.megamind.mycashpoint.domain.model.Transaction
import org.megamind.mycashpoint.domain.model.TransactionType
import org.megamind.mycashpoint.domain.repository.TransactionRepository
import org.megamind.mycashpoint.utils.Result

class GetTransactionsByCriteriaUseCase(private val repository: TransactionRepository) {

    operator fun invoke(
        codeAgence: String,
        operateurId: Long? = null,
        deviseCode: String? = null,
        type: TransactionType? = null,
        page: Int,
        size: Int
    ): Flow<Result<List<Transaction>>> {
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
