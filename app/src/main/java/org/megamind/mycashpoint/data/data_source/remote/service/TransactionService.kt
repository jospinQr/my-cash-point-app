package org.megamind.mycashpoint.data.data_source.remote.service

import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import org.megamind.mycashpoint.data.data_source.remote.dto.transaction.TransactionRequest
import org.megamind.mycashpoint.data.data_source.remote.dto.transaction.TransactionResponse
import org.megamind.mycashpoint.data.data_source.remote.safeApiCall
import org.megamind.mycashpoint.ui.screen.main.utils.Result

class TransactionService(
    private val httpClient: HttpClient,

) {

    suspend fun save(transactionRequest: TransactionRequest): Result<TransactionResponse> {


        return safeApiCall<TransactionResponse> {
            httpClient.post("transaction") {
                setBody(transactionRequest)
            }
        }
    }
}