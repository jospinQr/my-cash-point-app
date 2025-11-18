package org.megamind.mycashpoint.data.data_source.remote.dto.auth

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(

    val username: String,
    val password: String,
)
