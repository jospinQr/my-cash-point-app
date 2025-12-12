package org.megamind.mycashpoint.domain.repository

import kotlinx.coroutines.flow.Flow
import org.megamind.mycashpoint.domain.model.Solde
import org.megamind.mycashpoint.domain.model.SoldeType
import org.megamind.mycashpoint.utils.Constants
import org.megamind.mycashpoint.utils.Result

interface SoldeRepository {

    fun getSoldeByOperateurEtTypeEtDevise(
        idOperateur: Long,
        devise: String,
        soldeType: SoldeType
    ): Flow<Result<Solde?>>

    fun getAll(): Flow<Result<List<Solde>>>

    fun insertOrUpdate(solde: Solde): Flow<Result<Unit>>


    fun deleteByOperateurEtDevise(
        idOperateur: Long,
        devise: Constants.Devise
    ): Flow<Result<Unit>>

    fun getUnsyncedSoldes(): Flow<Result<List<Solde>>>

    fun markAsSynced(solde: Solde): Flow<Result<Unit>>


    //remote
    fun syncSoldes(): Flow<Result<Unit>>


    fun getSoldeFromServerByCriteria(
        codeAgence: String,
        operateurId: Long,
        deviseCode: String,
        soldeType: SoldeType
    ): Flow<Result<Solde>>


    fun getSoldeInRupture(): Flow<Result<List<Solde>>>

}
