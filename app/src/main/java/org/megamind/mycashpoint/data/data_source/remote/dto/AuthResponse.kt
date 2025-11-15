package org.megamind.mycashpoint.data.data_source.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(val token: String)