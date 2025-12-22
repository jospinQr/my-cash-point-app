package org.megamind.mycashpoint.data.data_source.remote.service

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import org.megamind.mycashpoint.data.data_source.remote.dto.etablissement.EtablissementRequest
import org.megamind.mycashpoint.data.data_source.remote.dto.etablissement.EtablissementResponse
import org.megamind.mycashpoint.data.data_source.remote.safeApiCall
import org.megamind.mycashpoint.utils.Result

class EtablissementService(private val httpClient: HttpClient) {


    suspend fun getEtablissement(id: Long = 1): Result<EtablissementResponse> {
        return safeApiCall<EtablissementResponse> {
            httpClient.get("etablisement/$id")
        }
    }

    suspend fun updateEtablissement(
        id: Long,
        request: EtablissementRequest
    ): Result<Unit> {
        return safeApiCall<Unit> {
            httpClient.put("etablisement/$id") {
                setBody(request)
            }
        }
    }


}