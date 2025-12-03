package org.megamind.mycashpoint.domain.model

import org.megamind.mycashpoint.utils.Constants
import java.math.BigDecimal

data class TopOperateur(

    val operateurNom: String,
    val montantTotal: BigDecimal,
    val nombreTransactions: Int,
    val operateurId: Int,
    val devise: String
)


