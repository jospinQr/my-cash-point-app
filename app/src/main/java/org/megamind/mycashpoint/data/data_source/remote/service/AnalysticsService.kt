package org.megamind.mycashpoint.data.data_source.remote.service

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import org.megamind.mycashpoint.data.data_source.remote.dto.analytics.DashboardResponse
import org.megamind.mycashpoint.data.data_source.remote.safeApiCall
import org.megamind.mycashpoint.utils.Result

class AnalyticsService(private val httpClient: HttpClient) {

    suspend fun getAgenceAnalytics(
        codeAgence: String,
        startDate: Long?,
        endDate: Long?
    ): Result<DashboardResponse> {
        return safeApiCall<DashboardResponse> {
            httpClient.get("analytics/dashboard/agence/$codeAgence") {
                parameter("startDate", startDate)
                parameter("endDate", endDate)
            }
        }
    }


}