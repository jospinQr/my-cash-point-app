package org.megamind.mycashpoint.data.data_source.remote.service

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import org.megamind.mycashpoint.data.data_source.remote.safeApiCall
import org.megamind.mycashpoint.utils.Result

class ExcelService(private val httpClient: HttpClient) {


    suspend fun generateGrandLivre(
        codeAgence: String, startDate: Long, endDate: Long
    ): Result<ByteArray> {

        return safeApiCall<ByteArray> {

            httpClient.get("mouvements-solde/agence/$codeAgence/rapport/excel") {
                parameter("startDate", startDate)
                parameter("endDate", endDate)
            }


        }

    }


    suspend fun generateJournalTransaction(
        codeAgence: String, startDate: Long, endDate: Long
    ): Result<ByteArray> {
        return safeApiCall<ByteArray> {

            httpClient.get("transaction/agence/$codeAgence/rapport/excel") {
                parameter("startDate", startDate)
                parameter("endDate", endDate)
            }


        }


    }


    suspend fun generateJournalOperationInterne(
        codeAgence: String, startDate: Long, endDate: Long
    ): Result<ByteArray> {
        return safeApiCall<ByteArray> {

            httpClient.get("operation-caisse/agence/$codeAgence/export/excel") {
                parameter("startDate", startDate)
                parameter("endDate", endDate)
            }


        }


    }


}