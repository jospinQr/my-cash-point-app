package org.megamind.mycashpoint.domain.model

import java.math.BigDecimal

data class Analytics(
    val agenceCode: String,
    val periodStart: Long,
    val periodEnd: Long,
    val totalVolume: BigDecimal,
    val transactionCount: Long,
    val volumeByType: Map<TransactionType, BigDecimal>,
    val countByType: Map<TransactionType, Long>,
    val topOperateur: String? = null
)