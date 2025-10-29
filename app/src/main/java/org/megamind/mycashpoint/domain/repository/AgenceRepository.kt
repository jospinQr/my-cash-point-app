package org.megamind.mycashpoint.domain.repository

import kotlinx.coroutines.flow.Flow
import org.megamind.mycashpoint.data.data_source.local.entity.Agence
import org.megamind.mycashpoint.utils.Result

interface AgenceRepository {

    suspend fun saveOrUpdate(agence: Agence): Flow<Result<Unit>>
    suspend fun getAll(): Flow<Result<List<Agence>>>
    suspend fun getById(id: String): Flow<Result<Agence?>>
    suspend fun deleteById(id: String): Flow<Result<Unit>>
}




