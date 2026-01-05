package org.megamind.mycashpoint.data.data_source.remote.dto.transaction

import kotlinx.serialization.Serializable
import org.megamind.mycashpoint.data.data_source.remote.dto.PaginationDTO
import org.megamind.mycashpoint.data.data_source.remote.dto.serializer.BigDecimalSerializer
import org.megamind.mycashpoint.domain.model.Transaction
import java.math.BigDecimal

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


@Serializable
data class TransactionPageResponse(
    val content: List<TransactionResponse>,
    val page: Int,
    val size: Int,
    val totalElements: Long,
    val totalPages: Int
)

@Serializable
data class TransactionsResponseDto(
    val transactions: TransactionPageResponse,
    @Serializable(with = BigDecimalSerializer::class)
    val totalMontant: BigDecimal
)
