package org.megamind.mycashpoint.data.data_source.remote.dto.auth

import kotlinx.serialization.Serializable

@Serializable
data class EditUserRequeste(
    val currentPassword: String,
    val newPassword: String,
    val newUsername: String,
)