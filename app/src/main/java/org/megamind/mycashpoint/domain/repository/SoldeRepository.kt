package org.megamind.mycashpoint.domain.repository

import kotlinx.coroutines.flow.Flow
import org.megamind.mycashpoint.data.data_source.local.entity.SoldeEntity
import org.megamind.mycashpoint.utils.Constants
import org.megamind.mycashpoint.utils.Result

interface SoldeRepository {

    suspend fun getSoldeByOperateurEtDevise(
        idOperateur: String,
        devise: Constants.Devise
    ): Flow<Result<SoldeEntity?>>

    suspend fun getAll(): Flow<Result<List<SoldeEntity>>>

    suspend fun insertOrUpdate(solde: SoldeEntity): Flow<Result<Unit>>

    suspend fun updateMontant(
        idOperateur: String,
        devise: Constants.Devise,
        montant: Long
    ): Flow<Result<Unit>>

    suspend fun deleteByOperateurEtDevise(
        idOperateur: String,
        devise: Constants.Devise
    ): Flow<Result<Unit>>
}

