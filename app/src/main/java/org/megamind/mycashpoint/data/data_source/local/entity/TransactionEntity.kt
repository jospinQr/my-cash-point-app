package org.megamind.mycashpoint.data.data_source.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Index
import org.megamind.mycashpoint.domain.model.StatutSync
import org.megamind.mycashpoint.domain.model.TransactionType
import org.megamind.mycashpoint.utils.Constants
import java.math.BigDecimal


@Entity(
    tableName = "flux_caisse",
    indices = [
        Index("transactionCode", unique = true),
        Index("idOperateur"),
        Index("horodatage")
    ],


    )
data class TransactionEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val transactionCode: String,
    val idOperateur: Int,             // ex: "AIRTEL"
    val type: TransactionType,                  // Type de mouvement
    val montant: BigDecimal,

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
    val creePar: Long,
    val codeAgence: String,
    val statutSync: StatutSync = StatutSync.EN_ATTENTE,

    )