package org.megamind.mycashpoint.data.data_source.repositoryImpl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.megamind.mycashpoint.data.data_source.local.dao.SoldeDao
import org.megamind.mycashpoint.data.data_source.local.dao.TransactionDao
import org.megamind.mycashpoint.data.data_source.local.entity.StatutSync
import org.megamind.mycashpoint.data.data_source.local.entity.TransactionEntity
import org.megamind.mycashpoint.domain.repository.TransactionRepository
import org.megamind.mycashpoint.utils.Constants
import org.megamind.mycashpoint.utils.Result

class TransactionRepositoryImpl(
    private val transactionDao: TransactionDao,
    private val soldeDao: SoldeDao
) : TransactionRepository {

    override suspend fun insert(transaction: TransactionEntity): Flow<Result<Unit>> = flow {
        try {
            emit(Result.Loading)
            transactionDao.insert(transaction)
            emit(Result.Succes(Unit))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }

    override suspend fun getAll(): Flow<Result<List<TransactionEntity>>> = flow {
        try {
            emit(Result.Loading)
            val transactions = transactionDao.getAll()
            emit(Result.Succes(transactions))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }

    override suspend fun getByOperateurEtDevice(
        idOperateur: String,
        device: Constants.Devise
    ): Flow<Result<List<TransactionEntity>>> = flow {
        try {
            emit(Result.Loading)
            val transactions = transactionDao.getByOperateurEtDevice(idOperateur, device)
            emit(Result.Succes(transactions))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }

    override suspend fun getBySyncStatus(statut: StatutSync): Flow<Result<List<TransactionEntity>>> =
        flow {
            try {
                emit(Result.Loading)
                val transactions = transactionDao.getBySyncStatus(statut)
                emit(Result.Succes(transactions))
            } catch (e: Exception) {
                emit(Result.Error(e))
            }
        }

    override suspend fun deleteById(id: String): Flow<Result<Unit>> = flow {
        try {
            emit(Result.Loading)
            transactionDao.deleteById(id)
            emit(Result.Succes(Unit))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }

    override suspend fun ajouterTransactionEtMettreAJourSolde(
        transaction: TransactionEntity
    ): Flow<Result<Unit>> = flow {
        try {
            emit(Result.Loading)
            transactionDao.ajouterTransactionEtMettreAJourSolde(transaction, soldeDao)
            emit(Result.Succes(Unit))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }
}

