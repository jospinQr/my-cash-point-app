package org.megamind.mycashpoint.domain.repository

import kotlinx.coroutines.flow.Flow
import org.megamind.mycashpoint.data.data_source.remote.dto.operation.OperationCaisseRequest
import org.megamind.mycashpoint.utils.Result

interface OperationCaisseRepository {
    suspend fun saveOperation(operationCaisseRequest: OperationCaisseRequest): Flow<Result<Unit>>
}
