package org.megamind.mycashpoint.data.data_source.remote.dto.agence

import kotlinx.serialization.Serializable

@Serializable
data class AgenceRequest(val code: String, val name: String)