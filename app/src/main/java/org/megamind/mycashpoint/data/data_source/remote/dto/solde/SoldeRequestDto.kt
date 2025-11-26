package org.megamind.mycashpoint.data.data_source.remote.dto.solde

import kotlinx.serialization.Serializable

@Serializable
data class SoldeRequestDto(
    val operateurId: Long,
    val soldeType: String,
    val montant: Double,
    val deviseCode: String,
    val seuilAlerte: Double?,
    val misAJourPar: Long,
    val dernierMiseAJour: Long,
    val codeAgence: String
)
