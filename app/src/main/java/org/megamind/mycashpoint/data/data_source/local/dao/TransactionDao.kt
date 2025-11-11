package org.megamind.mycashpoint.data.data_source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import org.megamind.mycashpoint.data.data_source.local.entity.SoldeEntity

import org.megamind.mycashpoint.data.data_source.local.entity.TransactionEntity
import org.megamind.mycashpoint.domain.model.SoldeType
import org.megamind.mycashpoint.domain.model.StatutSync
import org.megamind.mycashpoint.domain.model.TransactionType
import org.megamind.mycashpoint.utils.Constants
import java.math.BigDecimal

@Dao
interface TransactionDao {

    // --- CRUD de base ---
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertAndReturnId(transaction: TransactionEntity): Long

    @Query("SELECT * FROM flux_caisse WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): TransactionEntity?

    @Query("UPDATE flux_caisse SET transactionCode = :codeTransaction WHERE id = :id")
    suspend fun updateCodeTransaction(id: Long, codeTransaction: String)

    @Query("SELECT * FROM flux_caisse ORDER BY horodatage DESC")
    suspend fun getAll(): List<TransactionEntity>

    @Query("DELETE FROM flux_caisse WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query(""" SELECT * FROM flux_caisse WHERE idOperateur = :idOperateur AND device = :device ORDER BY horodatage DESC """)
    suspend fun getTransactionsByOperatorAndDevice(
        idOperateur: Int,
        device: Constants.Devise
    ): List<TransactionEntity>


    @Query("SELECT * FROM flux_caisse WHERE statutSync = :statut")
    suspend fun getBySyncStatus(statut: StatutSync): List<TransactionEntity>

    // --- Méthode atomique pour insertion et mise à jour des soldes ---
    @Transaction
    suspend fun insertTransactionAndUpdateSoldes(
        transaction: TransactionEntity,
        soldeDao: SoldeDao
    ): Long {
        val idOp = transaction.idOperateur
        val devise = transaction.device
        val montant = transaction.montant
        val userId = transaction.creePar

        suspend fun getOrCreateSolde(type: SoldeType): SoldeEntity {
            return soldeDao.getSoldeByOperateurEtTypeEtDevise(idOp, type, devise)
                ?: SoldeEntity(
                    idOperateur = idOp,
                    soldeType = type,
                    montant = BigDecimal.ZERO,
                    devise = devise,
                    misAJourPar = userId,
                    codeAgence = transaction.codeAgence
                )
        }

        // 🔹 Étape 1 : Vérification préalable AVANT TOUTE MODIFICATION
        when (transaction.type) {
            TransactionType.DEPOT -> {
                val soldeVirtuel = getOrCreateSolde(SoldeType.VIRTUEL)
                if (soldeVirtuel.montant < montant)
                    throw IllegalStateException("Solde virtuel insuffisant pour effectuer un dépôt.")
            }

            TransactionType.RETRAIT -> {
                val soldePhysique = getOrCreateSolde(SoldeType.PHYSIQUE)
                if (soldePhysique.montant < montant)
                    throw IllegalStateException("Solde physique insuffisant pour effectuer un retrait.")
            }

            TransactionType.TRANSFERT_SORTANT -> {
                val soldeVirtuel = getOrCreateSolde(SoldeType.VIRTUEL)
                if (soldeVirtuel.montant < montant)
                    throw IllegalStateException("Solde virtuel insuffisant pour le transfert sortant.")
            }

            else -> {}
        }

        // 🔹 Étape 2 : Mise à jour des soldes seulement si la vérif est passée
        suspend fun updateSolde(type: SoldeType, delta: BigDecimal): Pair<BigDecimal, BigDecimal> {
            val soldeActuel = getOrCreateSolde(type)
            val soldeAvant = soldeActuel.montant
            val nouveauMontant = soldeAvant + delta
            soldeDao.updateMontant(idOp, type, devise, nouveauMontant)
            return soldeAvant to nouveauMontant
        }

        var soldeAvant: BigDecimal? = null
        var soldeApres: BigDecimal? = null

        when (transaction.type) {
            TransactionType.DEPOT -> {
                updateSolde(SoldeType.VIRTUEL, -montant)
                val (avant, apres) = updateSolde(SoldeType.PHYSIQUE, montant)
                soldeAvant = avant; soldeApres = apres
            }

            TransactionType.RETRAIT -> {
                updateSolde(SoldeType.VIRTUEL, montant)
                val (avant, apres) = updateSolde(SoldeType.PHYSIQUE, -montant)
                soldeAvant = avant; soldeApres = apres
            }

            TransactionType.TRANSFERT_ENTRANT -> {
                val (avant, apres) = updateSolde(SoldeType.VIRTUEL, montant)
                soldeAvant = avant; soldeApres = apres
            }

            TransactionType.TRANSFERT_SORTANT -> {
                val (avant, apres) = updateSolde(SoldeType.VIRTUEL, -montant)
                soldeAvant = avant; soldeApres = apres
            }

            TransactionType.COMMISSION -> {
                val (avant, apres) = updateSolde(SoldeType.PHYSIQUE, montant)
                soldeAvant = avant; soldeApres = apres
            }
        }

        // 🔹 Étape 3 : Insérer la transaction uniquement si les vérifications sont passées
        val generatedId = insertAndReturnId(
            transaction.copy(
                transactionCode = "",
                soldeAvant = soldeAvant,
                soldeApres = soldeApres,
                horodatage = System.currentTimeMillis()
            )
        )

        val codeTransaction = String.format("%07d", generatedId) + transaction.codeAgence
        updateCodeTransaction(generatedId, codeTransaction)
        return generatedId
    }
}
