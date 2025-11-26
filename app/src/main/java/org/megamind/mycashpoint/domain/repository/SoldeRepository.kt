package org.megamind.mycashpoint.domain.repository

import kotlinx.coroutines.flow.Flow
import org.megamind.mycashpoint.domain.model.Solde
import org.megamind.mycashpoint.domain.model.SoldeType
import org.megamind.mycashpoint.ui.screen.main.utils.Constants
import org.megamind.mycashpoint.ui.screen.main.utils.Result
import java.math.BigDecimal

interface SoldeRepository {

    fun getSoldeByOperateurEtTypeEtDevise(
        idOperateur: Int,
        devise: String,
        soldeType: SoldeType
    ): Flow<Result<Solde?>>

    fun getAll(): Flow<Result<List<Solde>>>

    fun insertOrUpdate(solde: Solde): Flow<Result<Unit>>


    fun deleteByOperateurEtDevise(
        idOperateur: Int,
        devise: Constants.Devise
    ): Flow<Result<Unit>>


    fun sendSoldeToServeur(solde: Solde): Flow<Result<Unit>>


    fun getUnsyncedSoldes(): Flow<Result<List<Solde>>>

    fun markAsSynced(solde: Solde): Flow<Result<Unit>>

    fun syncSoldes(): Flow<Result<Unit>>
}
