package org.megamind.mycashpoint.data.data_source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Index
import java.util.UUID

enum class TypeFlux {
    DEPOT, RETRAIT, TRANSFERT_ENTRANT, TRANSFERT_SORTANT, COMMISSION
}

enum class StatutSync {
    EN_ATTENTE, SYNC, CONFLIT
}

/**
 * Représente un enregistrement de flux de caisse unique dans la base de données locale.
 *
 * Cette entité modélise chaque mouvement d'argent (dépôt, retrait, transfert, etc.)
 * lié à la caisse d'un opérateur de mobile money spécifique. Elle est conçue pour être stockée
 * dans une table de base de données Room nommée "flux_caisse".
 *
 * @property id Identifiant unique pour la transaction, généré sous forme d'UUID. C'est la clé primaire.
 * @property idSession Identifiant optionnel liant cette transaction à une `CashSessionEntity` spécifique.
 * @property idOperateur Identifiant de l'opérateur de mobile money concerné (ex: "AIRTEL", "ORANGE").
 * @property type Le type de flux de caisse, tel que défini par l'énumération [TypeFlux] (ex: DEPOT, RETRAIT).
 * @property montant Le montant absolu de la transaction, stocké dans la plus petite unité monétaire (ex: centimes).
 * @property soldeAvant Le solde de la caisse avant que cette transaction n'ait eu lieu. Peut être nul si non applicable.
 * @property soldeApres Le solde de la caisse après la finalisation de cette transaction. Peut être nul si non applicable.
 * @property reference Référence optionnelle pour la transaction, comme un numéro de reçu de l'opérateur.
 * @property note Note ou commentaire optionnel ajouté par l'utilisateur concernant la transaction.
 * @property horodatage L'horodatage (en millisecondes depuis l'époque) au moment de l'enregistrement de la transaction.
 * @property creePar L'identifiant de l'utilisateur local qui a créé cet enregistrement.
 * @property statutSync Le statut de synchronisation avec un serveur distant, défini par [StatutSync]. La valeur par défaut est `EN_ATTENTE`.
 */
@Entity(
    tableName = "flux_caisse",
    indices = [
        Index("idOperateur"),
        Index("idSession"),
        Index("horodatage")
    ]
)
data class FluxCaisseEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),

    val idSession: String? = null,       // Lien vers CashSession.id
    val idOperateur: String,             // ex: "AIRTEL"

    val type: TypeFlux,                  // Type de mouvement
    val montant: Long,                   // Montant en centimes

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
        TypeFlux.DEPOT, TypeFlux.TRANSFERT_ENTRANT -> montant
        TypeFlux.RETRAIT, TypeFlux.TRANSFERT_SORTANT, TypeFlux.COMMISSION -> -montant
    }
}
