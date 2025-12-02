package org.megamind.mycashpoint.data.data_source.remote.service

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import org.megamind.mycashpoint.data.data_source.remote.dto.agence.AgenceRequest
import org.megamind.mycashpoint.data.data_source.remote.dto.agence.AgenceResponse
import org.megamind.mycashpoint.data.data_source.remote.safeApiCall
import org.megamind.mycashpoint.utils.Result


class AgenceService(private val httpClient: HttpClient) {


    suspend fun save(agenceRequest: AgenceRequest): Result<AgenceResponse> {
        return safeApiCall<AgenceResponse> {
            httpClient.post("agence") {
                setBody(agenceRequest)

            }
        }
    }


    suspend fun getAll(): Result<List<AgenceResponse>> {

        return safeApiCall<List<AgenceResponse>> {
            httpClient.get("agence")
        }
    }

}