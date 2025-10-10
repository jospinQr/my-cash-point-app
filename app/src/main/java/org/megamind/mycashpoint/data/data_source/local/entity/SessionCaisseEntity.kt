package org.megamind.mycashpoint.data.data_source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Index
import java.util.UUID

@Entity(
    tableName = "sessions_caisse",
    indices = [
        Index("ouvertPar"),
        Index("ouvertLe")
    ]
)
data class SessionCaisse(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),

    val idOperateur: String? = null,      // Optionnel
    val ouvertPar: String?,               // Utilisateur local
    val ouvertLe: Long,                   // Timestamp

    val soldeOuverture: Long,             // Solde initial

    val fermePar: String? = null,         // Utilisateur à la fermeture
    val fermeLe: Long? = null,            // Timestamp
    val soldeFermeture: Long? = null,

    val estFermee: Boolean = false,
    val reconcilie: Boolean = false,      // Correspondance vérifiée

    val notes: String? = null,

    val statutSync: StatutSync = StatutSync.EN_ATTENTE,
    val idServeur: String? = null
)