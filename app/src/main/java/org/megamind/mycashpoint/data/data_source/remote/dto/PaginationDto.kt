package org.megamind.mycashpoint.data.data_source.remote.dto

import kotlinx.serialization.Serializable


@Serializable
data class PaginationDTO(
    val page: Int,
    val size: Int,
    val totalPages: Int,
    val totalElements: Int
)
