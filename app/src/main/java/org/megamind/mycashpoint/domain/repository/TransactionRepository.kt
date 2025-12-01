package org.megamind.mycashpoint.domain.repository

import kotlinx.coroutines.flow.Flow
import org.megamind.mycashpoint.data.data_source.local.entity.TransactionEntity
import org.megamind.mycashpoint.domain.model.PaginatedTransaction
import org.megamind.mycashpoint.domain.model.Transaction
import org.megamind.mycashpoint.domain.model.TransactionType
import org.megamind.mycashpoint.utils.Constants
import org.megamind.mycashpoint.utils.Result

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


    fun syncTransation(): Flow<Result<Unit>>

    fun getFromServerByCriteria(
        codeAgence: String,
        operateurId: Long,
        deviseCode: String,
        type: TransactionType,
        page: Int,
        size: Int,
    ): Flow<Result<PaginatedTransaction>>

    fun generateTransactionResponse(
        codeAgence: String,
        operateurId: Long,
        deviseCode: String,
        type: TransactionType,
        startDate: Long?,
        endDate: Long?
    ): Flow<Result<ByteArray>>


}

