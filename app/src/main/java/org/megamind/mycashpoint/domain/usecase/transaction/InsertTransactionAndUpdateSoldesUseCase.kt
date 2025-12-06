package org.megamind.mycashpoint.domain.usecase.transaction

import kotlinx.coroutines.flow.Flow
import org.megamind.mycashpoint.domain.model.Transaction
import org.megamind.mycashpoint.domain.repository.TransactionRepository
import org.megamind.mycashpoint.utils.Result

class InsertTransactionAndUpdateSoldesUseCase(
    private val repository: TransactionRepository
) {
    operator fun invoke(transaction: Transaction): Flow<Result<Transaction>> =
        repository.insertTransactionAndUpdateSoldes(transaction)
}

