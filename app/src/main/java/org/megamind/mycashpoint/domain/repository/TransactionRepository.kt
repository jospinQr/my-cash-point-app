package org.megamind.mycashpoint.domain.repository

import kotlinx.coroutines.flow.Flow
import org.megamind.mycashpoint.data.data_source.local.entity.TransactionEntity
import org.megamind.mycashpoint.domain.model.StatutSync
import org.megamind.mycashpoint.domain.model.Transaction
import org.megamind.mycashpoint.utils.Constants
import org.megamind.mycashpoint.utils.Result

interface TransactionRepository {

    fun insert(transaction: Transaction): Flow<Result<Unit>>

    fun allTransactions(): Flow<Result<List<Transaction>>>

    fun getTransactionsByOperatorAndDevice(
        idOperateur: Int,
        device: Constants.Devise
    ): Flow<Result<List<TransactionEntity>>>

    fun getTransactionsBySyncStatus(statut: StatutSync): Flow<Result<List<Transaction>>>

    fun deleteTransactionById(id: Long): Flow<Result<Unit>>

    fun insertTransactionAndUpdateSoldes(
        transaction: Transaction,
    ): Flow<Result<Transaction>>
}

