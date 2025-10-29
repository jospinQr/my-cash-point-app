package org.megamind.mycashpoint.data.data_source.repositoryImpl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import org.megamind.mycashpoint.data.data_source.local.dao.AgenceDao
import org.megamind.mycashpoint.data.data_source.local.entity.Agence
import org.megamind.mycashpoint.domain.repository.AgenceRepository
import org.megamind.mycashpoint.utils.Result


class AgenceRepositoryImpl(private val agenceDao: AgenceDao) : AgenceRepository {


    override suspend fun saveOrUpdate(agence: Agence): Flow<Result<Unit>> = flow {
        emit(Result.Loading)
        agenceDao.insertOrUpdate(agence)
        emit(Result.Succes(Unit))

    }.catch {
        emit(Result.Error(it))
    }

    override suspend fun getAll(): Flow<Result<List<Agence>>> = flow {
        emit(Result.Loading)
        val agences = agenceDao.getAll()
        emit(Result.Succes(agences))
    }.catch {
        emit(Result.Error(it))

    }

    override suspend fun getById(id: String): Flow<Result<Agence?>> = flow {
        emit(Result.Loading)
        val agence = agenceDao.getByID(id)
        emit(Result.Succes(agence))
    }.catch {
        emit(Result.Error(it))

    }

    override suspend fun deleteById(id: String): Flow<Result<Unit>> = flow {
        emit(Result.Loading)
        agenceDao.deleteById(id)
        emit(Result.Succes(Unit))
    }.catch {
        emit(Result.Error(it))


    }
}