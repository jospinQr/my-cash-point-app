package org.megamind.mycashpoint.data.data_source.remote.dto.solde

import kotlinx.serialization.Serializable

@Serializable
data class SoldeUpdateAmountRequestDto(
    val codeAgence: String,
    val soldeType: String,
    val deviseCode: String,
    val montant: Double,
    val misAJourPar: Long,
    val dernierMiseAJour: Long
)
