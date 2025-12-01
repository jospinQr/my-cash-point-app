package org.megamind.mycashpoint.domain.usecase.rapport

import kotlinx.coroutines.flow.Flow
import org.megamind.mycashpoint.data.data_source.local.entity.TransactionEntity
import org.megamind.mycashpoint.domain.repository.TransactionRepository
import org.megamind.mycashpoint.utils.Constants
import org.megamind.mycashpoint.utils.Result

class GetTransactionsByOperatorAndDeviceUseCase(private val repository: TransactionRepository) {


    operator fun invoke(
        idOperateur: Int,
        device: Constants.Devise
    ): Flow<Result<List<TransactionEntity>>> =
        repository.getTransactionsByOperatorAndDevice(idOperateur, device)


}