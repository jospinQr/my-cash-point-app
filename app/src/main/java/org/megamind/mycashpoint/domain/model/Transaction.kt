package org.megamind.mycashpoint.domain.model

import kotlinx.serialization.Serializable
import org.megamind.mycashpoint.R
import org.megamind.mycashpoint.utils.Constants
import java.math.BigDecimal

data class Transaction(


    val id: Long = 0,
    val idOperateur: Long = 0,             // ex: "AIRTEL"
    val transactionCode: String = "",
    val type: TransactionType = TransactionType.DEPOT,                  // Type de mouvement
    val montant: BigDecimal = BigDecimal.ZERO,

    val nomClient: String? = null,// Montant en centimes
    val numClient: String? = null,

    val commission: Float? = null,

    val nomBeneficaire: String? = null,
    val numBeneficaire: String? = null,

    val soldeAvant: BigDecimal? = null,
    val soldeApres: BigDecimal? = null,

    val devise: Constants.Devise,
    val reference: String? = null,
    val note: String? = null,
    val horodatage: Long = System.currentTimeMillis(),
    val creePar: Long = 0,
    val codeAgence: String = "",

    )


@Serializable
enum class TransactionType(val icon: Int, val label: String) {
    DEPOT(R.drawable.depot, "DEPOT"),
    RETRAIT(R.drawable.retrait, "RETRAIT"),

}

