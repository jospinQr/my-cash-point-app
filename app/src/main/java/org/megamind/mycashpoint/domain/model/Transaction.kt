package org.megamind.mycashpoint.domain.model

import androidx.room.PrimaryKey
import org.megamind.mycashpoint.utils.Constants
import java.math.BigDecimal
import java.util.UUID

data class Transaction(


    val id: Long = 0,
    val idOperateur: Int = 0,             // ex: "AIRTEL"
    val transactionCode: String = "",
    val type: TransactionType = TransactionType.DEPOT,                  // Type de mouvement
    val montant: BigDecimal = BigDecimal.ZERO,

    val nomClient: String? = null,// Montant en centimes
    val numClient: String? = null,

    val nomBeneficaire: String? = null,
    val numBeneficaire: String? = null,

    val soldeAvant: BigDecimal? = null,
    val soldeApres: BigDecimal? = null,

    val device: Constants.Devise,
    val reference: String? = null,       // Ex: reçu opérateur
    val note: String? = null,
    val horodatage: Long = System.currentTimeMillis(),
    val creePar: Long = 0,
    val codeAgence: String = "",
    val statutSync: StatutSync = StatutSync.EN_ATTENTE,

    )


enum class TransactionType {
    DEPOT, RETRAIT, TRANSFERT_ENTRANT, TRANSFERT_SORTANT, COMMISSION
}

enum class StatutSync {
    EN_ATTENTE, SYNC, CONFLIT
}
