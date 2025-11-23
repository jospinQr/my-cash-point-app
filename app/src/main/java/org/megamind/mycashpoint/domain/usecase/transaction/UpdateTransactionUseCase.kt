package org.megamind.mycashpoint.domain.usecase.transaction

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import org.megamind.mycashpoint.domain.model.Transaction
import org.megamind.mycashpoint.domain.repository.TransactionRepository
import org.megamind.mycashpoint.ui.screen.main.utils.Result

class UpdateTransactionUseCase(
    private val repository: TransactionRepository
) {
    operator fun invoke(transaction: Transaction): Flow<Result<Unit>> = flow {
        if (transaction.id <= 0L) {
            emit(Result.Error(IllegalArgumentException("TRANSACTION_ID_INVALID")))
            return@flow
        }
        val error = validateTransaction(transaction)
        if (error != null) {
            emit(Result.Error(error))
            return@flow
        }
        emitAll(repository.updateTransaction(transaction))
    }
}

private fun validateTransaction(transaction: Transaction): TransactionValidationException? {
    return when {
        transaction.idOperateur <= 0 ->
            TransactionValidationException.FieldRequired(TransactionField.OPERATEUR)
        transaction.montant <= java.math.BigDecimal.ZERO ->
            TransactionValidationException.InvalidAmount
        transaction.nomClient.isNullOrBlank() ->
            TransactionValidationException.FieldRequired(TransactionField.NOM_CLIENT)
        transaction.numClient.isNullOrBlank() ->
            TransactionValidationException.FieldRequired(TransactionField.TEL_CLIENT)
        else -> null
    }
}


