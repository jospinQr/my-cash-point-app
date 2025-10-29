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
    val idOperateur: Int,             // ex: "AIRTEL"
    val type: TypTransct,                  // Type de mouvement
    val montant: Double,

    val nomClient: String? = null,// Montant en centimes
    val numClient: String? = null,

    val nomBeneficaire: String? = null,
    val numBeneficaire: String? = null,

    val soldeAvant: Double? = null,
    val soldeApres: Double? = null,

    val device: Constants.Devise,
    val reference: String? = null,       // Ex: reçu opérateur
    val note: String? = null,
    val horodatage: Long = System.currentTimeMillis(),
    val creePar: String? = null,         // Utilisateur local
    val statutSync: StatutSync = StatutSync.EN_ATTENTE,

    ) {
    fun montantSigne(): Double = when (type) {
        TypTransct.DEPOT, TypTransct.TRANSFERT_ENTRANT -> montant
        TypTransct.RETRAIT, TypTransct.TRANSFERT_SORTANT, TypTransct.COMMISSION -> -montant
    }
}
