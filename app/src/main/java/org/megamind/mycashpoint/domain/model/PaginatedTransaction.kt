package org.megamind.mycashpoint.domain.model

data class PaginatedTransaction(
    val items: List<Transaction>,
    val pagination: Pagination,
    val totalMontant: Double
)
