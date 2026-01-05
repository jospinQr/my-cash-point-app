package org.megamind.mycashpoint.data.data_source.remote.service

import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import org.megamind.mycashpoint.data.data_source.remote.dto.operation.OperationCaisseRequest
import org.megamind.mycashpoint.data.data_source.remote.safeApiCall
import org.megamind.mycashpoint.utils.Result

class OperationCaisseService(
    private val httpClient: HttpClient
) {
    suspend fun save(operationRequest: OperationCaisseRequest): Result<Unit> {
        return safeApiCall<Unit> {
            httpClient.post("operation-caisse") {
                setBody(operationRequest)
            }
        }
    }
}
