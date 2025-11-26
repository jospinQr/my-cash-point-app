package org.megamind.mycashpoint.data.repositoryImpl

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.megamind.mycashpoint.data.data_source.local.dao.SoldeDao
import org.megamind.mycashpoint.data.data_source.local.dao.TransactionDao
import org.megamind.mycashpoint.data.data_source.local.entity.TransactionEntity
import org.megamind.mycashpoint.data.data_source.local.mapper.toTransaction
import org.megamind.mycashpoint.data.data_source.local.mapper.toTransactionEntity
import org.megamind.mycashpoint.data.data_source.remote.mapper.toTransactionRequest
import org.megamind.mycashpoint.data.data_source.remote.service.TransactionService
import org.megamind.mycashpoint.domain.model.Transaction
import org.megamind.mycashpoint.domain.repository.TransactionRepository
import org.megamind.mycashpoint.ui.screen.main.utils.Constants
import org.megamind.mycashpoint.ui.screen.main.utils.Result

class TransactionRepositoryImpl(
    private val transactionService: TransactionService,
    private val transactionDao: TransactionDao,
    private val soldeDao: SoldeDao
) : TransactionRepository {


    val TAG = "TransactionRepo"


    override fun insertTransactionAndUpdateSoldes(
        transaction: Transaction,
    ): Flow<Result<Transaction>> = flow {
        try {

            emit(Result.Loading)
            val transactionEntity = transaction.toTransactionEntity(isSynced = false)
            val id = transactionDao.insertTransactionAndUpdateSoldes(transactionEntity, soldeDao)
            val reloaded = transactionDao.getById(id)?.toTransaction()
            emit(Result.Success(reloaded))

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

    override fun getUnSyncedTransaction(): Flow<Result<List<Transaction>>> =
        flow {
            try {
                emit(Result.Loading)
                val transactions =
                    transactionDao.getUnSyncedTransaction().map { it.toTransaction() }
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
            val entity = transaction.toTransactionEntity(isSynced = false)
            transactionDao.updateTransactionAndUpdateSoldes(entity, soldeDao)
            emit(Result.Success(Unit))

        } catch (e: Exception) {

            emit(Result.Error(e))
        }
    }

    override fun sendOneTransactToServer(transaction: Transaction): Flow<Result<Unit>> = flow {

        try {

            Log.i(TAG, "begin Send")
            emit(Result.Loading)
            when (val result = transactionService.save(transaction.toTransactionRequest())) {
                is Result.Success -> {

                    transactionDao.makeAsSync(transaction.id)
                    emit(Result.Success(Unit))
                    Log.i(TAG, "Sended")


                }

                is Result.Error -> {
                    emit(Result.Error(result.e ?: Exception("Erreur inconnue lors de l'envoi")))
                    Log.e(TAG, result.e?.message ?: "Erreur inconnue")
                    Log.d(TAG, transaction.toTransactionRequest().toString())
                }

                else -> {
                    emit(Result.Error(Exception("État inattendu lors de l'envoi")))
                }
            }
        } catch (e: Exception) {
            emit(Result.Error(e))
            Log.e(TAG, e.message ?: "Erreur inconnue")
        }
    }

    override fun markAsSynced(transaction: Transaction): Flow<Result<Unit>> {
        TODO("Not yet implemented")
    }

    override fun syncTransation(): Flow<Result<Unit>> = flow {


        try {
            emit(Result.Loading)
            val unSyncedTransaction = transactionDao.getUnSyncedTransaction()

            if (unSyncedTransaction.isEmpty()) {
                emit(Result.Success(Unit))
                return@flow
            }


            unSyncedTransaction.forEach { transactionEntity ->

                val transaction = transactionEntity.toTransaction()
                val result = transactionService.save(transaction.toTransactionRequest())

                if (result is Result.Success) {
                    transactionDao.makeAsSync(transaction.id)
                } else if (result is Result.Error) {

                    throw result.e
                        ?: Exception("Impossible de synchroniser la transaction ${transaction.id}")
                }


            }
            emit(Result.Success(Unit))

        } catch (e: Exception) {
            emit(Result.Error(e))
            Log.e(TAG, e.message.toString())
        }


    }


}

