package org.megamind.mycashpoint.domain.repository

import kotlinx.coroutines.flow.Flow
import org.megamind.mycashpoint.data.data_source.local.entity.SoldeEntity
import org.megamind.mycashpoint.utils.Constants
import org.megamind.mycashpoint.utils.Result

interface SoldeRepository {

    suspend fun getSoldeByOperateurEtDevise(
        idOperateur: Int,
        devise: String,
    ): Flow<Result<SoldeEntity?>>

    suspend fun getAll(): Flow<Result<List<SoldeEntity>>>

    suspend fun insertOrUpdate(solde: SoldeEntity): Flow<Result<Unit>>

    suspend fun updateMontant(
        idOperateur: Int,
        devise: Constants.Devise,
        montant: Double
    ): Flow<Result<Unit>>

    suspend fun deleteByOperateurEtDevise(
        idOperateur: Int,
        devise: Constants.Devise
    ): Flow<Result<Unit>>
}

