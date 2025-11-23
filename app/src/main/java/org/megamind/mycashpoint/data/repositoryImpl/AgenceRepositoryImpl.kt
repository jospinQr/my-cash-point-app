package org.megamind.mycashpoint.data.repositoryImpl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import org.megamind.mycashpoint.data.data_source.local.dao.AgenceDao
import org.megamind.mycashpoint.data.data_source.local.mapper.toAgence
import org.megamind.mycashpoint.data.data_source.local.mapper.toAgenceEntity
import org.megamind.mycashpoint.domain.model.Agence
import org.megamind.mycashpoint.domain.repository.AgenceRepository
import org.megamind.mycashpoint.ui.screen.main.utils.Result


class AgenceRepositoryImpl(private val agenceDao: AgenceDao) : AgenceRepository {


    override fun saveOrUpdate(agence: Agence): Flow<Result<Unit>> = flow {
        emit(Result.Loading)
        agenceDao.insertOrUpdate(agence.toAgenceEntity())
        emit(Result.Success(Unit))

    }.catch {
        emit(Result.Error(it))
    }

    override fun getAll(): Flow<Result<List<Agence>>> = flow {
        emit(Result.Loading)
        val agences = agenceDao.getAll().map { it.toAgence() }
        emit(Result.Success(agences))
    }.catch {
        emit(Result.Error(it))

    }

    override  fun getById(id: String): Flow<Result<Agence?>> = flow {
        emit(Result.Loading)
        val agence = agenceDao.getByID(id)
        emit(Result.Success(agence?.toAgence()))
    }.catch {
        emit(Result.Error(it))

    }

    override  fun deleteById(id: String): Flow<Result<Unit>> = flow {
        emit(Result.Loading)
        agenceDao.deleteById(id)
        emit(Result.Success(Unit))
    }.catch {
        emit(Result.Error(it))


    }
}