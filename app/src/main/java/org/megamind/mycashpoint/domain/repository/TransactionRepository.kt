package org.megamind.mycashpoint.domain.repository

import kotlinx.coroutines.flow.Flow
import org.megamind.mycashpoint.data.data_source.local.entity.TransactionEntity
import org.megamind.mycashpoint.domain.model.Solde
import org.megamind.mycashpoint.domain.model.Transaction
import org.megamind.mycashpoint.ui.screen.main.utils.Constants
import org.megamind.mycashpoint.ui.screen.main.utils.Result

interface TransactionRepository {


    fun allTransactions(): Flow<Result<List<Transaction>>>

    fun getTransactionsByOperatorAndDevice(
        idOperateur: Int,
        device: Constants.Devise
    ): Flow<Result<List<TransactionEntity>>>

    fun getUnSyncedTransaction(): Flow<Result<List<Transaction>>>

    fun deleteTransactionById(id: Long): Flow<Result<Unit>>

    fun insertTransactionAndUpdateSoldes(
        transaction: Transaction,
    ): Flow<Result<Transaction>>

    fun updateTransaction(transaction: Transaction): Flow<Result<Unit>>


    fun sendOneTransactToServer(transaction: Transaction): Flow<Result<Unit>>

    fun markAsSynced(transaction: Transaction): Flow<Result<Unit>>

    fun syncTransation(): Flow<Result<Unit>>
}

