package org.megamind.mycashpoint.domain.usecase.transaction

import org.megamind.mycashpoint.domain.repository.TransactionRepository

class GetTransactionForSynUseCase(private val repository: TransactionRepository) {
    operator fun invoke(
        agenceCode: String,
        lastSyncAt: Long,
        userId: Long
    ) = repository.getTransactionForSync(agenceCode, lastSyncAt, userId)
}



