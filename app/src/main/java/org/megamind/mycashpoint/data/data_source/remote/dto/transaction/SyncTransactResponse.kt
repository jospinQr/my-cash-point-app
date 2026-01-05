package org.megamind.mycashpoint.data.data_source.remote.dto.transaction

import kotlinx.serialization.Serializable

@Serializable
data class SyncTransactResponse(val serverTime: Long, val transactions: List<TransactionResponse>)