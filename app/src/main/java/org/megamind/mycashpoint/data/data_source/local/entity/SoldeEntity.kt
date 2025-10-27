package org.megamind.mycashpoint.data.data_source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Index
import org.megamind.mycashpoint.utils.Constants

/**
 * Représente le solde pour un opérateur de mobile money spécifique dans la base de données locale.
 * Cette entité est stockée dans la table "soldes".
 *
 * @property idOperateur Un identifiant unique pour l'opérateur (ex: "AIRTEL", "ORANGE"). Ceci sert de clé primaire.
 * @property montant Le montant actuel du solde, stocké dans la plus petite unité monétaire (ex: centimes).
 * @property devise Le code de la devise du solde (ex: "CDF"). La valeur par défaut est "CDF".
 * @property dernierMiseAJour L'horodatage (timestamp en millisecondes) de la dernière mise à jour du solde.
 * @property seuilAlerte Un montant seuil optionnel. Si le solde tombe en dessous de cette valeur, une alerte peut être déclenchée. Peut être nul.
 * @property misAJourPar Un identifiant optionnel pour l'utilisateur qui a effectué la dernière mise à jour de ce solde, utile dans les scénarios multi-utilisateurs. Peut être nul.
 */
@Entity(
    tableName = "soldes",
    indices = [
        Index(value = ["idOperateur"], unique = true)
    ]
)
data class SoldeEntity(
    @PrimaryKey
    val idOperateur: String,         // ex : "AIRTEL", "ORANGE"

    val montant: Long,               // Solde actuel en centimes
    val devise: Constants.Devise,      // Devise

    val dernierMiseAJour: Long,      // Timestamp
    val seuilAlerte: Long? = null,   // Optionnel : seuil d'alerte

    // Pour multi-utilisateur
    val misAJourPar: String? = null  // ex: "user_123"
)
