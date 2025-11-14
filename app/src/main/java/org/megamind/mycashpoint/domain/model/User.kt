package org.megamind.mycashpoint.domain.model


data class User(
    val id: Long,
    val name: String,
    val idAgence: String,
    val role: String,
)