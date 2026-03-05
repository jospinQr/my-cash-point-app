package org.megamind.mycashpoint.domain.model


data class Etablissement(
    val id: Long=0,
    val name: String,
    val addrees: String,
    val contat: String?,
    val rccm: String?
)