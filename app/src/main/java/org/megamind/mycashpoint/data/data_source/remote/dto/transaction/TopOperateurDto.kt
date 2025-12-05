package org.megamind.mycashpoint.data.data_source.remote.dto.transaction

import kotlinx.serialization.Serializable
import org.megamind.mycashpoint.domain.model.TopOperateur

@Serializable
data class TopOperateurDto(
    val nombreTransactions: Int,
    val operateurNom: String,

    )


fun TopOperateurDto.toTopOperateur(): TopOperateur {

    return TopOperateur(
        nombreTransactions = nombreTransactions,
        operateurNom = operateurNom
    )
}
