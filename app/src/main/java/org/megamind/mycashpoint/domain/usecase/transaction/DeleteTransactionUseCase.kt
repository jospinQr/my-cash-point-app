package org.megamind.mycashpoint.domain.usecase.transaction

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.emitAll
import org.megamind.mycashpoint.domain.repository.TransactionRepository
import org.megamind.mycashpoint.utils.Result

class DeleteTransactionUseCase(
    private val repository: TransactionRepository
) {
    operator fun invoke(transactionId: Long): Flow<Result<Unit>> = flow {
        if (transactionId <= 0L) {
            emit(Result.Error(IllegalArgumentException("TRANSACTION_ID_INVALID")))
            return@flow
        }
        emitAll(repository.deleteTransactionById(transactionId))
    }
}


