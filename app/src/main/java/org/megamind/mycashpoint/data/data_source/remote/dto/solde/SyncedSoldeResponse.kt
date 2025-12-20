package org.megamind.mycashpoint.data.data_source.remote.dto.solde

import kotlinx.serialization.Serializable

@Serializable
data class SyncedSoldeResponse(
    val serverTime: Long,
    val soldes: List<SoldeResponse>
)
