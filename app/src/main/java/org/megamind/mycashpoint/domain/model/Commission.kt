package org.megamind.mycashpoint.domain.model

import androidx.room.PrimaryKey
import org.megamind.mycashpoint.ui.screen.main.utils.Constants
import java.math.BigDecimal

data class Commission(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val idOperateur: Int,
    val type: TransactionType,
    val devise: Constants.Devise,
    val taux: Double,
    val montantFixe: BigDecimal? = null // Optionnel
)
