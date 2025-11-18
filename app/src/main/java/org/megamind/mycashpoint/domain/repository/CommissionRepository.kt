package org.megamind.mycashpoint.domain.repository

import kotlinx.coroutines.flow.Flow
import org.megamind.mycashpoint.domain.model.Commission
import org.megamind.mycashpoint.domain.model.CommissionStats
import org.megamind.mycashpoint.domain.model.TransactionType
import org.megamind.mycashpoint.utils.Constants
import org.megamind.mycashpoint.utils.Result

interface CommissionRepository {

    fun insert(commission: Commission): Flow<Result<Unit>>

    fun insertAll(commissions: List<Commission>): Flow<Result<Unit>>

    fun update(commission: Commission): Flow<Result<Unit>>

    fun delete(commission: Commission): Flow<Result<Unit>>

    fun clearAll(): Flow<Result<Unit>>

    fun getAll(): Flow<Result<List<Commission>>>

    fun getById(id: Long): Flow<Result<Commission?>>

    fun getByOperateur(idOperateur: Int): Flow<Result<List<Commission>>>

    fun getCommission(
        idOperateur: Int,
        type: TransactionType,
        devise: Constants.Devise
    ): Flow<Result<Commission?>>

    fun getStatsParOperateur(): Flow<Result<List<CommissionStats>>>

    fun getStatsParDevise(): Flow<Result<List<CommissionStats>>>

    fun search(idOperateur: Int, query: String): Flow<Result<List<Commission>>>
}










