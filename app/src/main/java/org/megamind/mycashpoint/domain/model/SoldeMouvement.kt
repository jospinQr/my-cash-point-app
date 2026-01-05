package org.megamind.mycashpoint.domain.model

import java.math.BigDecimal

data class SoldeMouvement(
    val id: Long,
    val soldeId: Long,
    val soldeType: String, // PHYSIQUE | VIRTUEL
    val devise: String,    // USD | CDF=
    val montantAvant: BigDecimal,
    val montantApres: BigDecimal,
    val montantChange: BigDecimal,
    val typeMouvement: String, // CREATION | TRANSACTION
    val referenceContext: String? = null,
    val dateMouvement: Long,
    val auteurId: Long,
    val auteurName: String,
    val motif: String
)