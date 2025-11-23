package org.megamind.mycashpoint.data.repositoryImpl

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.megamind.mycashpoint.data.data_source.local.dao.SoldeDao
import org.megamind.mycashpoint.data.data_source.local.mapper.toSolde
import org.megamind.mycashpoint.data.data_source.local.mapper.toSoldeEntity
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

    override fun sendSoldeToServeur(solde: Solde): Flow<Result<Unit>> = flow {

        try {
            emit(Loading)

            when (val result = soldeService.save(solde.toSoldeRequestDto())) {
                is Success -> {
                    Log.i(TAG, "Sended")
                    emit(Success(Unit))

                }

                is Error -> {
                    Log.e(TAG, result.e?.message ?: "Erreur inconnue")
                    emit(Error(result.e ?: Exception("Erreur inconnue lors de l'envoi")))

                }

                else -> {
                    Log.e(TAG, "Erreur inattendu")
                    emit(Error(Exception("État inattendu lors de l'envoi")))
                }
            }
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
                emit(Success(Unit))
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

                    Log.e(TAG, "Failed to sync solde ${solde.idOperateur}: ${result.e?.message}")
                    throw result.e ?: Exception("Failed to sync solde ${solde.idOperateur}")
                }
            }
            emit(Success(Unit))
        } catch (e: Exception) {
            emit(Error(e))
        }
    }
}


