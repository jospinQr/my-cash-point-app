package org.megamind.mycashpoint.data.data_source.remote.service

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import org.megamind.mycashpoint.data.data_source.remote.dto.solde.PaginateSoldeMouvementResponse
import org.megamind.mycashpoint.data.data_source.remote.dto.solde.SoldeMouvementDto
import org.megamind.mycashpoint.data.data_source.remote.dto.solde.SoldeRequestDto
import org.megamind.mycashpoint.data.data_source.remote.dto.solde.SoldeResponse
import org.megamind.mycashpoint.data.data_source.remote.dto.solde.SoldeUpdateAmountRequestDto
import org.megamind.mycashpoint.data.data_source.remote.dto.solde.SyncedSoldeResponse
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


    suspend fun getSoldeInRupture(): Result<List<SoldeResponse>> {
        return safeApiCall<List<SoldeResponse>> {
            httpClient.get("solde/rupture")
        }
    }


    suspend fun getSoldeByUserAndAgence(
        userId: Long,
        codeAgence: String
    ): Result<List<SoldeResponse>> {

        return safeApiCall<List<SoldeResponse>> {
            httpClient.get("solde/agence/$codeAgence/updated-by/$userId")

        }

    }


    suspend fun getSoldeForSync(agenceCode: String, lastSyncAt: Long): Result<SyncedSoldeResponse> {

        return safeApiCall<SyncedSoldeResponse> {
            httpClient.get("solde/sync") {
                parameter("agenceCode", agenceCode)
                parameter("lastSyncAt", lastSyncAt)
            }
        }

    }


    suspend fun getSoldeMouvements(
        agenceCode: String,
        page: Int,
        size: Int
    ): Result<PaginateSoldeMouvementResponse> {

        return safeApiCall<PaginateSoldeMouvementResponse> {
            httpClient.get("mouvements-solde/agence") {
                parameter("agenceCode", agenceCode)
                parameter("page", page)
                parameter("size", size)
            }
        }

    }

    /**
     * Récupère le rapport Excel Grand Livre pour une agence
     * @param codeAgence Le code de l'agence
     * @param startDate Date de début (optionnel, en millisecondes)
     * @param endDate Date de fin (optionnel, en millisecondes)
     * @return ByteArray contenant le fichier Excel
     */
    suspend fun getGrandLivreExcelReport(
        codeAgence: String,
        startDate: Long? = null,
        endDate: Long? = null
    ): Result<ByteArray> {
        return safeApiCall<ByteArray> {
            httpClient.get("mouvements-solde/agence/$codeAgence/rapport/excel") {
                startDate?.let { parameter("startDate", it) }
                endDate?.let { parameter("endDate", it) }
            }
        }
    }
}

