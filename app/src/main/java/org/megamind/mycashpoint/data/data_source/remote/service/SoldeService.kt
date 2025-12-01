package org.megamind.mycashpoint.data.data_source.remote.service

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import org.megamind.mycashpoint.data.data_source.remote.dto.solde.SoldeRequestDto
import org.megamind.mycashpoint.data.data_source.remote.dto.solde.SoldeResponse
import org.megamind.mycashpoint.data.data_source.remote.dto.solde.SoldeUpdateAmountRequestDto
import org.megamind.mycashpoint.data.data_source.remote.safeApiCall
import org.megamind.mycashpoint.domain.model.SoldeType
import org.megamind.mycashpoint.utils.Result

class SoldeService(private val httpClient: HttpClient) {


    suspend fun save(soldeRequest: SoldeRequestDto): Result<SoldeResponse> {
        return safeApiCall<SoldeResponse> {
            httpClient.post("solde") {
                setBody(soldeRequest)
            }

        }
    }


    suspend fun updateAmount(
        operateurId: Long,
        soldeRequest: SoldeUpdateAmountRequestDto
    ): Result<SoldeResponse> {
        return safeApiCall<SoldeResponse> {
            httpClient.patch("solde/operateur/$operateurId/montant") {
                setBody(soldeRequest)
            }
        }
    }


    suspend fun getSoldeByCriteria(
        codeAgence: String,
        operateurId: Long,
        deviseCode: String,
        soldeType: SoldeType
    ): Result<SoldeResponse> {

        return safeApiCall<SoldeResponse> {
            httpClient.get("solde/agence/$codeAgence/operateur/$operateurId/type/${soldeType.name}/devise/$deviseCode")
        }
    }


}


