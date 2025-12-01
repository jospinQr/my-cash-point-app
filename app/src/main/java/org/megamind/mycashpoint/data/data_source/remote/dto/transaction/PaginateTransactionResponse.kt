package org.megamind.mycashpoint.data.data_source.remote.dto.transaction

import kotlinx.serialization.Serializable
import org.megamind.mycashpoint.data.data_source.remote.dto.PaginationDTO

@Serializable
data class PaginateTransactionsResponse(
    val items: List<TransactionResponse>,
    val pagination: PaginationDTO,
    val totalMontant: Double
)