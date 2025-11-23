package org.megamind.mycashpoint.domain.usecase.transaction

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.megamind.mycashpoint.domain.model.Transaction
import org.megamind.mycashpoint.domain.model.TransactionType
import org.megamind.mycashpoint.domain.repository.TransactionRepository
import org.megamind.mycashpoint.ui.screen.main.utils.Result
import java.math.BigDecimal

class InsertTransactionAndUpdateSoldesUseCase(
    private val repository: TransactionRepository
) {
    operator fun invoke(transaction: Transaction): Flow<Result<Transaction>> = flow {

        val error = validate(transaction)
        if (error != null) {
            emit(Result.Error(error))
            return@flow
        }

        repository.insertTransactionAndUpdateSoldes(transaction).collect { emit(it) }
    }
}


private fun validate(transaction: Transaction): TransactionValidationException? {
    return when {
        transaction.idOperateur <= 0 ->
            TransactionValidationException.FieldRequired(TransactionField.OPERATEUR)
        transaction.montant <= BigDecimal.ZERO ->
            TransactionValidationException.InvalidAmount
        transaction.type == TransactionType.DEPOT && transaction.nomBeneficaire.isNullOrBlank() ->
            TransactionValidationException.FieldRequired(TransactionField.NOM_BENEF)
        transaction.type == TransactionType.DEPOT && transaction.numBeneficaire.isNullOrBlank() ->
            TransactionValidationException.FieldRequired(TransactionField.TEL_BENEF)
        transaction.nomClient.isNullOrBlank() ->
            TransactionValidationException.FieldRequired(TransactionField.NOM_CLIENT)
        transaction.numClient.isNullOrBlank() ->
            TransactionValidationException.FieldRequired(TransactionField.TEL_CLIENT)
        else -> null
    }
}

enum class TransactionField {
    OPERATEUR,
    TYPE,
    DEVISE,
    NOM_CLIENT,
    TEL_CLIENT,
    NOM_BENEF,
    TEL_BENEF,
}

sealed class TransactionValidationException(message: String) : IllegalArgumentException(message) {
    class FieldRequired(val field: TransactionField) : TransactionValidationException("FIELD_REQUIRED:${'$'}field")
    data object InvalidAmount : TransactionValidationException("INVALID_AMOUNT")
}


