package org.megamind.mycashpoint.domain.repository

import kotlinx.coroutines.flow.Flow
import org.megamind.mycashpoint.data.data_source.local.entity.AgenceEntity
import org.megamind.mycashpoint.domain.model.Agence
import org.megamind.mycashpoint.utils.Result

interface AgenceRepository {

    fun saveOrUpdate(agence: Agence): Flow<Result<Unit>>
    fun getAll(): Flow<Result<List<Agence>>>
     fun getById(id: String): Flow<Result<Agence?>>
     fun deleteById(id: String): Flow<Result<Unit>>
}




