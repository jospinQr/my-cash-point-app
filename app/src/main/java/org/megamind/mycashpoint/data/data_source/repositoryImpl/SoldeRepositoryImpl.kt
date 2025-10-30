package org.megamind.mycashpoint.data.data_source.repositoryImpl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.megamind.mycashpoint.data.data_source.local.dao.SoldeDao
import org.megamind.mycashpoint.data.data_source.local.entity.SoldeEntity
import org.megamind.mycashpoint.domain.repository.SoldeRepository
import org.megamind.mycashpoint.utils.Constants
import org.megamind.mycashpoint.utils.Result

class SoldeRepositoryImpl(private val soldeDao: SoldeDao) : SoldeRepository {

    override suspend fun getSoldeByOperateurEtDevise(
        idOperateur: Int,
        devise: String,
    ): Flow<Result<SoldeEntity?>> = flow {
        try {
            emit(Result.Loading)
            val solde = soldeDao.getSoldeByOperateurEtDevise(idOperateur, devise)
            emit(Result.Succes(solde))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }

    override suspend fun getAll(): Flow<Result<List<SoldeEntity>>> = flow {
        try {
            emit(Result.Loading)
            val soldes = soldeDao.getAll()
            emit(Result.Succes(soldes))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }

    override suspend fun insertOrUpdate(solde: SoldeEntity): Flow<Result<Unit>> = flow {
        try {
            emit(Result.Loading)
            soldeDao.insertOrUpdate(solde)
            emit(Result.Succes(Unit))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }

    override suspend fun updateMontant(
        idOperateur: Int,
        devise: Constants.Devise,
        montant: Double
    ): Flow<Result<Unit>> = flow {
        try {
            emit(Result.Loading)
            soldeDao.updateMontant(idOperateur, devise, montant)
            emit(Result.Succes(Unit))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }

    override suspend fun deleteByOperateurEtDevise(
        idOperateur: Int,
        devise: Constants.Devise
    ): Flow<Result<Unit>> = flow {
        try {
            emit(Result.Loading)
            soldeDao.deleteByOperateurEtDevise(idOperateur, devise)
            emit(Result.Succes(Unit))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }
}

