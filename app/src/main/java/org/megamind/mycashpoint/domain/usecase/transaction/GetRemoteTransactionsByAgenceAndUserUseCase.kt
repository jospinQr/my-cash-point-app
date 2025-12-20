package org.megamind.mycashpoint.domain.usecase.transaction

import kotlinx.coroutines.flow.Flow
import org.megamind.mycashpoint.domain.model.Transaction
import org.megamind.mycashpoint.domain.repository.TransactionRepository
import org.megamind.mycashpoint.utils.Result

class GetRemoteTransactionsByAgenceAndUserUseCase(private val transactionRepository: TransactionRepository) {


    operator fun invoke(
    ): Flow<Result<List<Transaction>>> {
        return transactionRepository.getRemoteTransactionsByAgenceAndUser()

    }


}