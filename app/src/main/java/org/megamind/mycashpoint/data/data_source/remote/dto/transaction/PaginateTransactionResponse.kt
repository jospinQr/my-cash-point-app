package org.megamind.mycashpoint.data.data_source.remote.dto.transaction

import kotlinx.serialization.Serializable
import org.megamind.mycashpoint.data.data_source.remote.dto.PaginationDTO
import org.megamind.mycashpoint.domain.model.Transaction

@Serializable
data class PaginateTransactionsResponse(
    val items: List<TransactionResponse>,
    val pagination: PaginationDTO,
    val totalMontant: Double
)
@Serializable

data class TransactionPageResponseDTO(
    val content: List<TransactionResponse>,
    val page: Int,
    val size: Int,
    val totalElements: Long,
    val totalPages: Int
)


