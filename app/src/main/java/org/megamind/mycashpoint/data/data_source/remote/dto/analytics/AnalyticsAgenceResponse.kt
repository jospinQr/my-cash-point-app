package org.megamind.mycashpoint.data.data_source.remote.dto.analytics

import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import org.megamind.mycashpoint.data.data_source.remote.dto.serializer.BigDecimalSerializer
import org.megamind.mycashpoint.data.data_source.remote.dto.serializer.VolumeByTypeSerializer
import org.megamind.mycashpoint.domain.model.Analytics
import org.megamind.mycashpoint.domain.model.TransactionType
import java.math.BigDecimal

@Serializable
data class DashboardResponse(
    val agenceCode: String,
    val periodStart: Long,
    val periodEnd: Long,
    @Serializable(with = BigDecimalSerializer::class)
    val totalVolume: BigDecimal,
    val transactionCount: Long,
    @Serializable(with = VolumeByTypeSerializer::class)
    val volumeByType: Map<TransactionType, BigDecimal>,
    val countByType: Map<TransactionType, Long>,
    val topOperateur: String? = null
)


fun DashboardResponse.toAnalytics(): Analytics {
    return Analytics(
        agenceCode = this.agenceCode,
        periodStart = this.periodStart,
        periodEnd = this.periodEnd,
        totalVolume = this.totalVolume,
        transactionCount = this.transactionCount,
        volumeByType = this.volumeByType,
        countByType = this.countByType,
        topOperateur = this.topOperateur
    )

}


