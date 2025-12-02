package org.megamind.mycashpoint.data.repositoryImpl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import org.megamind.mycashpoint.data.data_source.local.dao.AgenceDao
import org.megamind.mycashpoint.data.data_source.local.mapper.toAgence
import org.megamind.mycashpoint.data.data_source.local.mapper.toAgenceEntity
import org.megamind.mycashpoint.data.data_source.remote.mapper.toAgence
import org.megamind.mycashpoint.data.data_source.remote.mapper.toAgenceRequest
import org.megamind.mycashpoint.data.data_source.remote.service.AgenceService
import org.megamind.mycashpoint.domain.model.Agence
import org.megamind.mycashpoint.domain.repository.AgenceRepository
import org.megamind.mycashpoint.utils.Result


class AgenceRepositoryImpl(private val agenceService: AgenceService) : AgenceRepository {


    override fun saveOrUpdate(agence: Agence): Flow<Result<Unit>> = flow {

        val result = agenceService.save(agence.toAgenceRequest())

        when (result) {
            is Result.Success -> {

                emit(Result.Success(Unit))
            }

            is Result.Loading -> {
                emit(Result.Loading)
            }

            is Result.Error -> {
                emit(Result.Error(result.e ?: Exception("Erreur inconnue")))
            }
        }
    }.catch {
        emit(Result.Error(it))
    }

    override fun getAll(): Flow<Result<List<Agence>>> = flow {


        when (val result = agenceService.getAll()) {
            is Result.Loading -> {
                emit(Result.Loading)
            }

            is Result.Success -> {
                emit(Result.Success(result.data?.map { it.toAgence() }))
            }

            is Result.Error<*> -> {
                emit(Result.Error(result.e ?: Exception("Erreur inconnue")))
            }
        }

        emit(Result.Loading)


    }.catch {
        emit(Result.Error(it))

    }

    override fun getById(id: String): Flow<Result<Agence?>> = flow {

    }

    override fun deleteById(id: String): Flow<Result<Unit>> = flow {

    }


}
