package org.megamind.mycashpoint.domain.repository

import kotlinx.coroutines.flow.Flow
import org.megamind.mycashpoint.domain.model.Analytics
import org.megamind.mycashpoint.utils.Result

interface AnalyticsRepository {

    fun getAgenceAnalytics(
        codeAgence: String,
        startDate: Long?,
        endDate: Long?
    ): Flow<Result<Analytics>>
}