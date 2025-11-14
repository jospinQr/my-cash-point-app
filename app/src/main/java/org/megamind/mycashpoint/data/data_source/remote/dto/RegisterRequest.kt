package org.megamind.mycashpoint.data.data_source.remote.dto


data class RegisterRequest(
    val username: String,
    val password: String,
    val codeAgence: String?,
    val role: Role,

)

enum class Role {
    ADMIN,
    AGENT
}