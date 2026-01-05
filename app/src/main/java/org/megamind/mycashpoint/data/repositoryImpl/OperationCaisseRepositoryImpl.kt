package org.megamind.mycashpoint.data.repositoryImpl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.megamind.mycashpoint.data.data_source.remote.dto.operation.OperationCaisseRequest
import org.megamind.mycashpoint.data.data_source.remote.service.OperationCaisseService
import org.megamind.mycashpoint.domain.repository.OperationCaisseRepository
import org.megamind.mycashpoint.utils.Result

class OperationCaisseRepositoryImpl(
    private val api: OperationCaisseService
) : OperationCaisseRepository {
    override suspend fun saveOperation(operationCaisseRequest: OperationCaisseRequest): Flow<Result<Unit>> =
        flow {
            emit(Result.Loading)

            try {
                when (val result = api.save(operationCaisseRequest)) {
                    is Result.Success -> emit(Result.Success(Unit))
                    is Result.Error -> emit(Result.Error(result.e ?: Exception("Unknown error")))
                    else -> {}
                }
            } catch (e: Exception) {
                emit(Result.Error(e))
            }

        }
}
