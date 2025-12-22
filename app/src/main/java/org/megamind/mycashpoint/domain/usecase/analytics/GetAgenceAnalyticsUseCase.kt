package org.megamind.mycashpoint.domain.usecase.analytics

import org.megamind.mycashpoint.domain.repository.AnalyticsRepository

class GetAgenceAnalyticsUseCase(private val analyticsRepository: AnalyticsRepository) {

    operator fun invoke(codeAgence: String, startDate: Long?=null, endDate: Long?=null) =
        analyticsRepository.getAgenceAnalytics(codeAgence, startDate, endDate)

}
