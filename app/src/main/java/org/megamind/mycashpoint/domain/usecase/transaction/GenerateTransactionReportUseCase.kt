package org.megamind.mycashpoint.domain.usecase.transaction

import kotlinx.coroutines.flow.Flow
import org.megamind.mycashpoint.domain.model.TransactionType
import org.megamind.mycashpoint.domain.repository.TransactionRepository
import org.megamind.mycashpoint.utils.Result

class GenerateTransactionReportUseCase(private val repository: TransactionRepository) {

    operator fun invoke(
        codeAgence: String,
        operateurId: Long,
        deviseCode: String,
        type: TransactionType,
        startDate: Long?,
        endDate: Long?
    ): Flow<Result<ByteArray>> {
        return repository.generateTransactionResponse(
            codeAgence = codeAgence,
            operateurId = operateurId,
            deviseCode = deviseCode,
            type = type,
            startDate = startDate,
            endDate = endDate
        )
    }
}
