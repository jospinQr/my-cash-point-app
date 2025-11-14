package org.megamind.mycashpoint.data.repositoryImpl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import org.megamind.mycashpoint.data.data_source.local.dao.CommissionDao
import org.megamind.mycashpoint.data.data_source.local.mapper.toCommission
import org.megamind.mycashpoint.data.data_source.local.mapper.toCommissionEntity
import org.megamind.mycashpoint.data.data_source.local.mapper.toDomain
import org.megamind.mycashpoint.domain.model.Commission
import org.megamind.mycashpoint.domain.model.CommissionStats
import org.megamind.mycashpoint.domain.model.TransactionType
import org.megamind.mycashpoint.domain.repository.CommissionRepository
import org.megamind.mycashpoint.utils.Constants
import org.megamind.mycashpoint.utils.Result

class CommissionRepositoryImpl(
    private val commissionDao: CommissionDao
) : CommissionRepository {

    override fun insert(commission: Commission): Flow<Result<Unit>> = flow {
        try {
            emit(Result.Loading)
            commissionDao.insertCommission(commission.toCommissionEntity())
            emit(Result.Success(Unit))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }

    override fun insertAll(commissions: List<Commission>): Flow<Result<Unit>> = flow {
        try {
            emit(Result.Loading)
            commissionDao.insertAll(commissions.map { it.toCommissionEntity() })
            emit(Result.Success(Unit))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }

    override fun update(commission: Commission): Flow<Result<Unit>> = flow {
        try {
            emit(Result.Loading)
            commissionDao.updateCommission(commission.toCommissionEntity())
            emit(Result.Success(Unit))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }

    override fun delete(commission: Commission): Flow<Result<Unit>> = flow {
        try {
            emit(Result.Loading)
            commissionDao.deleteCommission(commission.toCommissionEntity())
            emit(Result.Success(Unit))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }

    override fun clearAll(): Flow<Result<Unit>> = flow {
        try {
            emit(Result.Loading)
            commissionDao.clearAll()
            emit(Result.Success(Unit))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }

    override fun getAll(): Flow<Result<List<Commission>>> =
        commissionDao.getAllCommissions().map { list ->
            try {
                Result.Success(list.map { it.toCommission() }) as Result<List<Commission>>
            } catch (e: Exception) {
                Result.Error(e)
            }
        }

    override fun getById(id: Long): Flow<Result<Commission?>> = flow {
        try {
            emit(Result.Loading)
            emit(Result.Success(commissionDao.getCommissionById(id)?.toCommission()))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }

    override fun getByOperateur(idOperateur: Int): Flow<Result<List<Commission>>> =
        commissionDao.getCommissionsByOperateur(idOperateur).map { list ->
            try {
                Result.Success(list.map { it.toCommission() }) as Result<List<Commission>>
            } catch (e: Exception) {
                Result.Error(e)
            }
        }

    override fun getCommission(
        idOperateur: Int,
        type: TransactionType,
        devise: Constants.Devise
    ): Flow<Result<Commission?>> = flow {
        try {
            emit(Result.Loading)
            emit(Result.Success(commissionDao.getCommission(idOperateur, type, devise)?.toCommission()))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }

    override fun getStatsParOperateur(): Flow<Result<List<CommissionStats>>> =
        commissionDao.getStatsParOperateur().map { list ->
            try {
                Result.Success(list.map { it.toDomain() }) as Result<List<CommissionStats>>
            } catch (e: Exception) {
                Result.Error(e)
            }
        }

    override fun getStatsParDevise(): Flow<Result<List<CommissionStats>>> =
        commissionDao.getStatsParDevise().map { list ->
            try {
                Result.Success(list.map { it.toDomain() }) as Result<List<CommissionStats>>
            } catch (e: Exception) {
                Result.Error(e)
            }
        }

    override fun search(idOperateur: Int, query: String): Flow<Result<List<Commission>>> =
        commissionDao.searchCommissions(idOperateur, query).map { list ->
            try {
                Result.Success(list.map { it.toCommission() }) as Result<List<Commission>>
            } catch (e: Exception) {
                Result.Error(e)
            }
        }
}








