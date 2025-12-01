package org.megamind.mycashpoint.domain.model

data class Pagination(
    val page: Int,
    val size: Int,
    val totalPages: Int,
    val totalElements: Int
)
