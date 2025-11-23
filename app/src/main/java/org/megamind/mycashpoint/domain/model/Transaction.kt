package org.megamind.mycashpoint.domain.model

import org.megamind.mycashpoint.R
import org.megamind.mycashpoint.ui.screen.main.utils.Constants
import java.math.BigDecimal

data class Transaction(


    val id: Long = 0,
    val idOperateur: Int = 0,             // ex: "AIRTEL"
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

    val device: Constants.Devise,
    val reference: String? = null,       // Ex: reçu opérateur
    val note: String? = null,
    val horodatage: Long = System.currentTimeMillis(),
    val creePar: Long = 0,
    val codeAgence: String = "",
    val statutSync: StatutSync = StatutSync.EN_ATTENTE,

    )


enum class TransactionType(val icon: Int) {
    DEPOT(R.drawable.depot), RETRAIT(R.drawable.retrait), TRANSFERT_ENTRANT(R.drawable.trans_entrant), TRANSFERT_SORTANT(
        R.drawable.trans_sortie
    ),
    COMMISSION(R.drawable.transaction_out)
}

enum class StatutSync { EN_ATTENTE, SYNC, CONFLIT }
