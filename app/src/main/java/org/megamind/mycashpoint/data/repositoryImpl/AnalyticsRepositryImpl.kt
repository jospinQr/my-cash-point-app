package org.megamind.mycashpoint.data.repositoryImpl

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.megamind.mycashpoint.data.data_source.remote.dto.analytics.toAnalytics
import org.megamind.mycashpoint.data.data_source.remote.service.AnalyticsService
import org.megamind.mycashpoint.domain.model.Analytics
import org.megamind.mycashpoint.domain.repository.AnalyticsRepository
import org.megamind.mycashpoint.utils.Result


class AnalyticsRepositryImpl(private val apiService: AnalyticsService) : AnalyticsRepository {

    val LOG = "AnalyticsRepositryImpl"

    override fun getAgenceAnalytics(
        codeAgence: String, startDate: Long?, endDate: Long?
    ): Flow<Result<Analytics>> = flow {


        emit(Result.Loading)
        Log.i(LOG, "Loading")
        try {

            when (val result = apiService.getAgenceAnalytics(codeAgence, startDate, endDate)) {

                is Result.Success -> {

                    emit(Result.Success(result.data?.toAnalytics()))

                }

                is Result.Error -> {
                    emit(Result.Error(result.e ?: Exception("Erreur inconnue")))
                    Log.e(LOG, "Error ${result.e?.message}")
                }

                else -> {}
            }


        } catch (e: Exception) {
            emit(Result.Error(e))
            Log.e(LOG, "Error ${e.message}")


        }


    }

}