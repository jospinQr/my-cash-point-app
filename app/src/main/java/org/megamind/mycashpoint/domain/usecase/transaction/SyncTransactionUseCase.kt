package org.megamind.mycashpoint.domain.usecase.transaction

import kotlinx.coroutines.flow.Flow
import org.megamind.mycashpoint.domain.repository.TransactionRepository
import org.megamind.mycashpoint.utils.Result

class SyncTransactionUseCase(private val transactionRepository: TransactionRepository) {

    operator fun invoke(): Flow<Result<Unit>> {
        return transactionRepository.syncTransation()
    }
}