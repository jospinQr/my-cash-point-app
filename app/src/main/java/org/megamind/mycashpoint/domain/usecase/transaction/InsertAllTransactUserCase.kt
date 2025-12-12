package org.megamind.mycashpoint.domain.usecase.transaction

import kotlinx.coroutines.flow.Flow
import org.megamind.mycashpoint.domain.model.Transaction
import org.megamind.mycashpoint.domain.repository.TransactionRepository
import org.megamind.mycashpoint.utils.Result

class InsertAllTransactUserCase(private val transactionRepository: TransactionRepository) {


    operator fun invoke(transactions: List<Transaction>): Flow<Result<Unit>> {
        return transactionRepository.insertAll(transactions)
    }


}