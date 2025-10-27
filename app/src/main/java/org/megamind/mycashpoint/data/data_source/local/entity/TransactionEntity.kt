package org.megamind.mycashpoint.data.data_source.local.entity

import android.health.connect.datatypes.Device
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Index
import org.megamind.mycashpoint.utils.Constants
import java.util.UUID

enum class TypTransct {
    DEPOT, RETRAIT, TRANSFERT_ENTRANT, TRANSFERT_SORTANT, COMMISSION
}

enum class StatutSync {
    EN_ATTENTE, SYNC, CONFLIT
}


@Entity(
    tableName = "flux_caisse",
    indices = [
        Index("idOperateur"),
        Index("horodatage")
    ]
)
data class TransactionEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),

    val idOperateur: String,             // ex: "AIRTEL"

    val type: TypTransct,                  // Type de mouvement
    val montant: Long,                   // Montant en centimes
    val device: Constants.Devise,
    val soldeAvant: Long? = null,        // Solde précédent
    val soldeApres: Long? = null,        // Solde après

    val reference: String? = null,       // Ex: reçu opérateur
    val note: String? = null,

    val horodatage: Long = System.currentTimeMillis(),

    val creePar: String? = null,         // Utilisateur local

    val statutSync: StatutSync = StatutSync.EN_ATTENTE,
    val idServeur: String? = null
) {
    fun montantSigne(): Long = when (type) {
        TypTransct.DEPOT, TypTransct.TRANSFERT_ENTRANT -> montant
        TypTransct.RETRAIT, TypTransct.TRANSFERT_SORTANT, TypTransct.COMMISSION -> -montant
    }
}
