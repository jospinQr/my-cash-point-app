package org.megamind.mycashpoint.data.data_source.remote.service

import io.ktor.client.HttpClient
import io.ktor.client.request.header
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import org.megamind.mycashpoint.data.data_source.remote.dto.transaction.TransactionRequest
import org.megamind.mycashpoint.data.data_source.remote.dto.transaction.TransactionResponse
import org.megamind.mycashpoint.data.data_source.remote.safeApiCall
import org.megamind.mycashpoint.utils.DataStorageManager
import org.megamind.mycashpoint.utils.Result

class TransactionService(
    private val httpClient: HttpClient,
    private val dataStorageManager: DataStorageManager
) {

    suspend fun save(transactionRequest: TransactionRequest): Result<TransactionResponse> {

        val token = dataStorageManager.getToken()
        return safeApiCall<TransactionResponse> {
            httpClient.post("transaction") {
                setBody(transactionRequest)
                header("Authorization", "Bearer $token")
            }
        }
    }
}