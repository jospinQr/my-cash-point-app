package org.megamind.mycashpoint.data.repositoryImpl

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import org.megamind.mycashpoint.data.data_source.local.dao.EtablissementDao
import org.megamind.mycashpoint.data.data_source.local.mapper.toEtablissement
import org.megamind.mycashpoint.data.data_source.local.mapper.toEntity
import org.megamind.mycashpoint.data.data_source.remote.dto.etablissement.toEtablissement
import org.megamind.mycashpoint.data.data_source.remote.dto.etablissement.toEtablissementRequest
import org.megamind.mycashpoint.data.data_source.remote.service.EtablissementService
import org.megamind.mycashpoint.domain.model.Etablissement
import org.megamind.mycashpoint.domain.repository.EtablissementRepository
import org.megamind.mycashpoint.utils.Result

class EtablissementRepositoryImpl(
    private val service: EtablissementService,
    private val dao: EtablissementDao
) : EtablissementRepository {

    private val TAG = "EtablissementRepository"

    override fun getEtablissementFromServer(): Flow<Result<Etablissement>> = flow {
        emit(Result.Loading)
        when (val result = service.getEtablissement()) {
            is Result.Success -> {
                result.data?.let { response ->
                    val domain = response.toEtablissement()
                    emit(Result.Success(domain))
                } ?: emit(Result.Error(Exception("Données vides")))
            }

            is Result.Error -> emit(Result.Error(result.e ?: Exception("Erreur inconnue")))
            is Result.Loading -> {}
        }
    }.catch { e ->
        Log.e(TAG, "Error fetching from server", e)
        emit(Result.Error(e))
    }

    override fun editEtablissement(etablissement: Etablissement): Flow<Result<Unit>> = flow {
        emit(Result.Loading)
        val request = etablissement.toEtablissementRequest()
        when (val result = service.updateEtablissement(etablissement.id, request)) {
            is Result.Success -> {
                emit(Result.Success(Unit))
            }

            is Result.Error -> emit(Result.Error(result.e ?: Exception("Erreur de mise à jour")))
            is Result.Loading -> {}
        }
    }.catch { e ->
        Log.e(TAG, "Error updating etablissement", e)
        emit(Result.Error(e))
    }

    override fun getEtablissementFromLocal(): Flow<Result<Etablissement>> = flow {
        emit(Result.Loading)
        val entity = dao.getEtablissement()
        emit(Result.Success(entity.toEtablissement()))
    }.catch { e ->
        Log.e(TAG, "Error fetching from local", e)
        emit(Result.Error(e))
    }

    override fun getFromServerAndInsertLocaly(): Flow<Result<Unit>> = flow {
        emit(Result.Loading)
        when (val result = service.getEtablissement()) {
            is Result.Success -> {
                result.data?.let { response ->
                    val domain = response.toEtablissement()
                    dao.insertEtablissement(domain.toEntity())
                    emit(Result.Success(Unit))
                } ?: emit(Result.Error(Exception("Données vides")))
            }

            is Result.Error -> emit(Result.Error(result.e ?: Exception("Erreur inconnue")))
            is Result.Loading -> {}
        }
    }.catch { e ->
        Log.e(TAG, "Error sync server to local", e)
        emit(Result.Error(e))
    }
}