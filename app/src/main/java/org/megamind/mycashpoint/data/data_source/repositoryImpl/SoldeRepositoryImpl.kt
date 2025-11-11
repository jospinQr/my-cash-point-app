package org.megamind.mycashpoint.data.data_source.repositoryImpl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.megamind.mycashpoint.data.data_source.local.dao.SoldeDao
import org.megamind.mycashpoint.data.data_source.local.mapper.toSolde
import org.megamind.mycashpoint.data.data_source.local.mapper.toSoldeEntity
import org.megamind.mycashpoint.domain.model.Solde
import org.megamind.mycashpoint.domain.model.SoldeType
import org.megamind.mycashpoint.domain.repository.SoldeRepository
import org.megamind.mycashpoint.utils.Constants
import org.megamind.mycashpoint.utils.Result
import java.math.BigDecimal

class SoldeRepositoryImpl(private val soldeDao: SoldeDao) : SoldeRepository {

    override fun getSoldeByOperateurEtTypeEtDevise(
        idOperateur: Int,
        devise: String,
        soldeType: SoldeType
    ): Flow<Result<Solde?>> = flow {
        try {
            emit(Result.Loading)
            val solde = soldeDao.getSoldeByOperateurEtTypeEtDevise(
                idOperateur = idOperateur,
                type = soldeType,
                devise = Constants.Devise.valueOf(devise)
            )
            emit(Result.Success(solde?.toSolde()))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }

    override fun getAll(): Flow<Result<List<Solde>>> = flow {
        try {
            emit(Result.Loading)
            val soldes = soldeDao.getAll().map { it.toSolde() }
            emit(Result.Success(soldes))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }

    override fun insertOrUpdate(solde: Solde): Flow<Result<Unit>> = flow {
        try {
            emit(Result.Loading)
            val soldeEntity = solde.toSoldeEntity()
            soldeDao.insertOrUpdate(soldeEntity)
            emit(Result.Success(Unit))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }

    override fun updateMontant(
        idOperateur: Int,
        devise: Constants.Devise,
        montant: BigDecimal,
        soldeType: SoldeType
    ): Flow<Result<Unit>> = flow {
        try {
            emit(Result.Loading)
            soldeDao.updateMontant(
                idOp = idOperateur,
                devise = devise,
                nouveauMontant = montant,
                type = soldeType
            )
            emit(Result.Success(Unit))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }

    override fun deleteByOperateurEtDevise(
        idOperateur: Int,
        devise: Constants.Devise
    ): Flow<Result<Unit>> = flow {
        try {
            emit(Result.Loading)
            soldeDao.deleteByOperateurEtDevise(idOperateur, devise)
            emit(Result.Success(Unit))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }
}


