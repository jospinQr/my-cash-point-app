package org.megamind.mycashpoint.domain.usecase.transaction

import org.megamind.mycashpoint.domain.repository.TransactionRepository

class GetAllTransactionFromServerUseCase(private val transactionRepository: TransactionRepository) {


    operator fun invoke(page: Int, size: Int) = transactionRepository.getAllTransactionFromServer(page, size)


}