package org.megamind.mycashpoint.domain.usecase.transaction

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.megamind.mycashpoint.domain.model.Transaction
import org.megamind.mycashpoint.domain.repository.TransactionRepository
import org.megamind.mycashpoint.utils.Result

class SendOneTransactToServerUseCase(private val transactionRepository: TransactionRepository) {
    operator fun invoke(transaction: Transaction): Flow<Result<Unit>> = flow {
        transactionRepository.sendOneTransactToServer(transaction).collect { emit(it) }
    }
}