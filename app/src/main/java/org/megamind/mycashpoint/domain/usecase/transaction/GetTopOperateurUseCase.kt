package org.megamind.mycashpoint.domain.usecase.transaction

import kotlinx.coroutines.flow.Flow
import org.megamind.mycashpoint.data.data_source.remote.dto.transaction.TopOperateurDto
import org.megamind.mycashpoint.domain.model.TopOperateur
import org.megamind.mycashpoint.domain.repository.TransactionRepository
import org.megamind.mycashpoint.utils.Constants
import org.megamind.mycashpoint.utils.Result

class GetTopOperateurUseCase(private val repository: TransactionRepository) {


    operator fun invoke(
        codeAgence: String, devise: Constants.Devise
    ): Flow<Result<List<TopOperateur>>> =
        repository.getTopTransactionByOperateur(codeAgence, devise)


}
