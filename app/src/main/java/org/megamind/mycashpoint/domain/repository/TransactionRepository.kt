package org.megamind.mycashpoint.domain.repository

import kotlinx.coroutines.flow.Flow
import org.megamind.mycashpoint.data.data_source.local.entity.StatutSync
import org.megamind.mycashpoint.data.data_source.local.entity.TransactionEntity
import org.megamind.mycashpoint.utils.Constants
import org.megamind.mycashpoint.utils.Result

interface TransactionRepository {

    suspend fun insert(transaction: TransactionEntity): Flow<Result<Unit>>

    suspend fun getAll(): Flow<Result<List<TransactionEntity>>>

    suspend fun getByOperateurEtDevice(
        idOperateur: String,
        device: Constants.Devise
    ): Flow<Result<List<TransactionEntity>>>

    suspend fun getBySyncStatus(statut: StatutSync): Flow<Result<List<TransactionEntity>>>

    suspend fun deleteById(id: String): Flow<Result<Unit>>

    suspend fun ajouterTransactionEtMettreAJourSolde(
        transaction: TransactionEntity
    ): Flow<Result<Unit>>
}

