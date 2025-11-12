package org.megamind.mycashpoint.domain.model

import org.megamind.mycashpoint.utils.Constants

data class CommissionStats(
    val idOperateur: Int? = null,
    val devise: Constants.Devise? = null,
    val nombreTaux: Int,
    val tauxMoyen: Double
)






