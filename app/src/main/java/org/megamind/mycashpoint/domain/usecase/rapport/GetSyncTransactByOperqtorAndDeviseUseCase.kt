package org.megamind.mycashpoint.domain.usecase.rapport

import kotlinx.coroutines.flow.Flow
import org.megamind.mycashpoint.data.data_source.local.entity.TransactionEntity
import org.megamind.mycashpoint.domain.model.Transaction
import org.megamind.mycashpoint.domain.repository.TransactionRepository
import org.megamind.mycashpoint.utils.Constants
import org.megamind.mycashpoint.utils.Result

class GetSyncTransactByOperatorAndDeviseUseCase(private val repository: TransactionRepository) {

    operator fun invoke(
        idOperateur: Long,
        device: Constants.Devise
    ): Flow<Result<List<Transaction>>> =
        repository.getSyncTransactByOperatorAndDevise(idOperateur, device)


}
