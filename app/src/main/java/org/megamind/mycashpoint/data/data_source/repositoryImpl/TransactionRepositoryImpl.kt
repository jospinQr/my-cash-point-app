package org.megamind.mycashpoint.data.data_source.repositoryImpl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.megamind.mycashpoint.data.data_source.local.dao.SoldeDao
import org.megamind.mycashpoint.data.data_source.local.dao.TransactionDao
import org.megamind.mycashpoint.data.data_source.local.entity.TransactionEntity
import org.megamind.mycashpoint.data.data_source.local.mapper.toTransaction
import org.megamind.mycashpoint.data.data_source.local.mapper.toTransactionEntity
import org.megamind.mycashpoint.domain.model.StatutSync
import org.megamind.mycashpoint.domain.model.Transaction
import org.megamind.mycashpoint.domain.repository.TransactionRepository
import org.megamind.mycashpoint.utils.Constants
import org.megamind.mycashpoint.utils.Result

class TransactionRepositoryImpl(
    private val transactionDao: TransactionDao,
    private val soldeDao: SoldeDao
) : TransactionRepository {

    override fun insert(transaction: Transaction): Flow<Result<Unit>> = flow {
        try {
            emit(Result.Loading)
            val transactionEntity = transaction.toTransactionEntity()
            transactionDao.insertAndReturnId(transactionEntity)
            emit(Result.Success(Unit))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }

    override fun allTransactions(): Flow<Result<List<Transaction>>> = flow {
        try {
            emit(Result.Loading)
            val transactions = transactionDao.getAll().map { it.toTransaction() }
            emit(Result.Success(transactions))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }

    override fun getTransactionsByOperatorAndDevice(
        idOperateur: Int,
        device: Constants.Devise
    ): Flow<Result<List<TransactionEntity>>> = flow {
        try {
            emit(Result.Loading)
            val transactions =
                transactionDao.getTransactionsByOperatorAndDevice(idOperateur, device)
            emit(Result.Success(transactions))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }

    override fun getTransactionsBySyncStatus(statut: StatutSync): Flow<Result<List<Transaction>>> =
        flow {
            try {
                emit(Result.Loading)
                val transactions = transactionDao.getBySyncStatus(statut).map { it.toTransaction() }
                emit(Result.Success(transactions))
            } catch (e: Exception) {
                emit(Result.Error(e))
            }
        }

    override fun deleteTransactionById(id: Long): Flow<Result<Unit>> = flow {
        try {
            emit(Result.Loading)
            val transaction = transactionDao.getById(id)
                ?: throw IllegalArgumentException("TRANSACTION_NOT_FOUND")
            transactionDao.deleteTransactionAndUpdateSoldes(transaction, soldeDao)
            emit(Result.Success(Unit))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }

    override fun updateTransaction(transaction: Transaction): Flow<Result<Unit>> = flow {
        try {
            emit(Result.Loading)
            val entity = transaction.toTransactionEntity()
            transactionDao.updateTransactionAndUpdateSoldes(entity, soldeDao)
            emit(Result.Success(Unit))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }

    override fun insertTransactionAndUpdateSoldes(
        transaction: Transaction,
    ): Flow<Result<Transaction>> = flow {
        try {

            emit(Result.Loading)
            val transactionEntity = transaction.toTransactionEntity()
            val id = transactionDao.insertTransactionAndUpdateSoldes(transactionEntity, soldeDao)
            val reloaded = transactionDao.getById(id)?.toTransaction()
            emit(Result.Success(reloaded))

        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }


}

