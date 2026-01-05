package org.megamind.mycashpoint.domain.usecase.transaction

import kotlinx.coroutines.flow.Flow
import org.megamind.mycashpoint.domain.model.Transaction
import org.megamind.mycashpoint.domain.repository.TransactionRepository
import org.megamind.mycashpoint.utils.Result

class GetTransactionByAgenceFromServerUseCase(private val transactionRepository: TransactionRepository) {

    operator fun invoke(codeAgence: String, page: Int, size: Int): Flow<Result<List<Transaction>>> {
        return transactionRepository.getTransactionByAgence(
            codeAgence = codeAgence,
            page = page,
            size = size
        )
    }
}


