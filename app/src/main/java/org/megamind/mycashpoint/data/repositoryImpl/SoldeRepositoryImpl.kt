package org.megamind.mycashpoint.data.repositoryImpl

import android.net.http.HttpException
import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.megamind.mycashpoint.data.data_source.local.dao.SoldeDao
import org.megamind.mycashpoint.data.data_source.local.mapper.toSolde
import org.megamind.mycashpoint.data.data_source.local.mapper.toSoldeEntity
import org.megamind.mycashpoint.data.data_source.remote.ApiException
import org.megamind.mycashpoint.data.data_source.remote.mapper.toSoldeRequestDto
import org.megamind.mycashpoint.data.data_source.remote.mapper.toSoldeUpdateAmountRequestDto
import org.megamind.mycashpoint.data.data_source.remote.service.SoldeService
import org.megamind.mycashpoint.domain.model.Solde
import org.megamind.mycashpoint.domain.model.SoldeType
import org.megamind.mycashpoint.domain.repository.SoldeRepository
import org.megamind.mycashpoint.ui.screen.main.utils.Constants
import org.megamind.mycashpoint.ui.screen.main.utils.Result
import org.megamind.mycashpoint.ui.screen.main.utils.Result.*
import java.math.BigDecimal

class SoldeRepositoryImpl(private val soldeDao: SoldeDao, private val soldeService: SoldeService) :
    SoldeRepository {

    val TAG = "SoldeRepo"
    override fun getSoldeByOperateurEtTypeEtDevise(
        idOperateur: Int,
        devise: String,
        soldeType: SoldeType
    ): Flow<Result<Solde?>> = flow {
        try {
            emit(Loading)
            val solde = soldeDao.getSoldeByOperateurEtTypeEtDevise(
                idOperateur = idOperateur,
                type = soldeType,
                devise = Constants.Devise.valueOf(devise)
            )
            emit(Success(solde?.toSolde()))
        } catch (e: Exception) {
            emit(Error(e))
        }
    }

    override fun getAll(): Flow<Result<List<Solde>>> = flow {
        try {
            emit(Loading)
            val soldes = soldeDao.getAll().map { it.toSolde() }
            emit(Success(soldes))
        } catch (e: Exception) {
            emit(Error(e))
        }
    }

    override fun insertOrUpdate(solde: Solde): Flow<Result<Unit>> = flow {
        try {
            emit(Loading)
            val soldeEntity = solde.toSoldeEntity(isSynced = false)
            soldeDao.insertOrUpdate(soldeEntity)
            emit(Success(Unit))
        } catch (e: Exception) {
            emit(Error(e))
        }
    }

    override fun deleteByOperateurEtDevise(
        idOperateur: Int,
        devise: Constants.Devise
    ): Flow<Result<Unit>> = flow {
        try {
            emit(Loading)
            soldeDao.deleteByOperateurEtDevise(idOperateur, devise)
            emit(Success(Unit))
        } catch (e: Exception) {
            emit(Error(e))
        }
    }



    override fun getUnsyncedSoldes(): Flow<Result<List<Solde>>> = flow {
        try {
            emit(Loading)
            val soldes = soldeDao.getUnsyncedSoldes().map { it.toSolde() }
            emit(Success(soldes))
        } catch (e: Exception) {
            emit(Error(e))
        }
    }

    override fun markAsSynced(solde: Solde): Flow<Result<Unit>> = flow {
        try {
            emit(Loading)
            soldeDao.markAsSynced(solde.idOperateur, solde.soldeType, solde.devise)
            emit(Success(Unit))
        } catch (e: Exception) {
            emit(Error(e))
        }
    }

    override fun syncSoldes(): Flow<Result<Unit>> = flow {
        try {
            emit(Loading)
            val unsyncedSoldes = soldeDao.getUnsyncedSoldes()
            if (unsyncedSoldes.isEmpty()) {
                emit(Error(Exception("Aucun solde à synchroniser")))
                return@flow
            }

            unsyncedSoldes.forEach { soldeEntity ->
                val solde = soldeEntity.toSolde()

                val result = soldeService.updateAmount(
                    solde.idOperateur.toLong(),
                    solde.toSoldeUpdateAmountRequestDto()
                )
                if (result is Success) {
                    soldeDao.markAsSynced(solde.idOperateur, solde.soldeType, solde.devise)
                } else if (result is Error) {

                    if (result.e is ApiException && result.e.code == 404) {

                        val soldeResult = soldeService.save(solde.toSoldeRequestDto())

                        if (soldeResult is Success) {
                            soldeDao.markAsSynced(solde.idOperateur, solde.soldeType, solde.devise)
                        } else {
                            throw result.e
                        }

                    } else {

                        throw result.e ?: Exception("Erreur inconnue lors de l'envoi")

                    }


                }
            }
            emit(Success(Unit))
        } catch (e: Exception) {
            emit(Error(e))
        }
    }
}


