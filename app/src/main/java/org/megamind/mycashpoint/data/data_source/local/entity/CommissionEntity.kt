package org.megamind.mycashpoint.data.data_source.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import org.megamind.mycashpoint.domain.model.TransactionType
import org.megamind.mycashpoint.ui.screen.main.utils.Constants
import java.math.BigDecimal

@Entity(
    tableName = "commissions",
    indices = [Index(value = ["idOperateur", "type", "devise"], unique = true)],
    )
data class CommissionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val idOperateur: Int,
    val type: TransactionType,
    val devise: Constants.Devise,
    val taux: Double,
    val montantFixe: BigDecimal? = null // Optionnel
)
