package org.megamind.mycashpoint.domain.repository

import kotlinx.coroutines.flow.Flow
import org.megamind.mycashpoint.domain.model.Solde
import org.megamind.mycashpoint.domain.model.SoldeType
import org.megamind.mycashpoint.utils.Constants
import org.megamind.mycashpoint.utils.Result
import java.math.BigDecimal

interface SoldeRepository {

    fun getSoldeByOperateurEtTypeEtDevise(
        idOperateur: Int,
        devise: String,
        soldeType: SoldeType
    ): Flow<Result<Solde?>>

    fun getAll(): Flow<Result<List<Solde>>>

    fun insertOrUpdate(solde: Solde): Flow<Result<Unit>>

    fun updateMontant(
        idOperateur: Int,
        devise: Constants.Devise,
        montant: BigDecimal,
        soldeType: SoldeType
    ): Flow<Result<Unit>>

    fun deleteByOperateurEtDevise(
        idOperateur: Int,
        devise: Constants.Devise
    ): Flow<Result<Unit>>
}


