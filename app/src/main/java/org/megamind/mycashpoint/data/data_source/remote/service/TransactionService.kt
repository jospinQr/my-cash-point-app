package org.megamind.mycashpoint.data.data_source.remote.service

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import org.megamind.mycashpoint.data.data_source.remote.dto.transaction.PaginateTransactionsResponse
import org.megamind.mycashpoint.data.data_source.remote.dto.transaction.TopOperateurDto
import org.megamind.mycashpoint.data.data_source.remote.dto.transaction.TransactionPageResponseDTO
import org.megamind.mycashpoint.data.data_source.remote.dto.transaction.TransactionRequest
import org.megamind.mycashpoint.data.data_source.remote.dto.transaction.TransactionResponse
import org.megamind.mycashpoint.data.data_source.remote.safeApiCall
import org.megamind.mycashpoint.domain.model.PaginatedTransaction
import org.megamind.mycashpoint.domain.model.TransactionType
import org.megamind.mycashpoint.utils.Constants
import org.megamind.mycashpoint.utils.Result

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


    suspend fun findByCriteria(
        codeAgence: String,
        operateurId: Long,
        deviseCode: String,
        type: TransactionType,
        page: Int = 0,
        size: Int = 100,
    ): Result<PaginateTransactionsResponse> {

        return safeApiCall<PaginateTransactionsResponse> {
            httpClient.get("transaction/agence/$codeAgence/filter") {
                parameter("operateurId", operateurId)
                parameter("deviseCode", deviseCode)
                parameter("type", type.name)
                parameter("page", page)
                parameter("size", size)
            }
        }
    }


    suspend fun generateTransactionRepport(
        codeAgence: String,
        operateurId: Long,
        deviseCode: String,
        type: TransactionType,
        startDate: Long?,
        endDate: Long?,
    ): Result<ByteArray> {

        return safeApiCall<ByteArray> {
            httpClient.get("transaction/agence/${codeAgence}/rapport/pdf") {
                parameter("operateurId", operateurId)
                parameter("devise", deviseCode)
                parameter("type", type.name)
                parameter("startDate", startDate)
                parameter("endDate", endDate)
            }
        }


    }


    suspend fun getTopTransactionByOperateur(
        codeAgence: String,
        devise: Constants.Devise
    ): Result<List<TopOperateurDto>> {

        return safeApiCall<List<TopOperateurDto>> {
            httpClient.get("transaction/agence/$codeAgence/top-operateurs") {
                parameter("devise", devise.name)
            }

        }

    }


    suspend fun getAllTransaction(
        page: Int = 0,
        size: Int = 50000
    ): Result<List<TransactionResponse>> {
        return safeApiCall<List<TransactionResponse>> {
            httpClient.get("transaction") {
                parameter("page", page)
                parameter("size", size)

            }
        }

    }


    suspend fun getTransactionByAgenceCodeAndUserId(
        agenceCode: String,
        userId: Long
    ): Result<TransactionPageResponseDTO> {
        return safeApiCall {
            httpClient.get("transaction/agence/$agenceCode/user/$userId")

        }
    }

}