package org.megamind.mycashpoint.data.data_source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import org.megamind.mycashpoint.data.data_source.local.entity.SoldeEntity
import org.megamind.mycashpoint.data.data_source.local.entity.TransactionEntity
import org.megamind.mycashpoint.domain.model.SoldeType
import org.megamind.mycashpoint.domain.model.TransactionType
import org.megamind.mycashpoint.utils.Constants
import java.math.BigDecimal

@Dao
interface TransactionDao {

    // --- CRUD de base ---
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertAndReturnId(transaction: TransactionEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(transactions: List<TransactionEntity>)


    @Query("SELECT * FROM flux_caisse WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): TransactionEntity?

    @Query("UPDATE flux_caisse SET transactionCode = :codeTransaction WHERE id = :id")
    suspend fun updateCodeTransaction(id: Long, codeTransaction: String)

    @Query("SELECT * FROM flux_caisse ORDER BY horodatage DESC")
    suspend fun getAll(): List<TransactionEntity>

    @Query("DELETE FROM flux_caisse WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Update
    suspend fun update(transaction: TransactionEntity)

    @Transaction
    suspend fun deleteTransactionAndUpdateSoldes(
        transaction: TransactionEntity,
        soldeDao: SoldeDao
    ) {
        adjustSoldesForTransaction(transaction, soldeDao, undo = true)
        deleteById(transaction.id)
    }

    @Transaction
    suspend fun updateTransactionAndUpdateSoldes(
        updatedTransaction: TransactionEntity,
        soldeDao: SoldeDao
    ) {
        val existing = getById(updatedTransaction.id)
            ?: throw IllegalArgumentException("TRANSACTION_NOT_FOUND")

        adjustSoldesForTransaction(existing, soldeDao, undo = true)
        val (soldeAvant, soldeApres) = adjustSoldesForTransaction(
            updatedTransaction,
            soldeDao,
            undo = false
        )

        update(
            updatedTransaction.copy(
                soldeAvant = soldeAvant,
                soldeApres = soldeApres,
                horodatage = System.currentTimeMillis()
            )
        )
    }

    @Query(""" SELECT * FROM flux_caisse WHERE idOperateur = :idOperateur AND device = :device AND isSynced=0 ORDER BY horodatage DESC """)
    suspend fun getNonSyncTransactByOperatorAndDevise(
        idOperateur: Long,
        device: Constants.Devise
    ): List<TransactionEntity>


    @Query(""" SELECT * FROM flux_caisse WHERE idOperateur = :idOperateur AND device = :device  ORDER BY horodatage DESC """)
    suspend fun getSyncTransactByOperatorAndDevise(
        idOperateur: Long,
        device: Constants.Devise
    ): List<TransactionEntity>


    @Query("SELECT * FROM flux_caisse WHERE isSynced = 0")
    suspend fun getUnSyncedTransaction(): List<TransactionEntity>


    // --- Méthode atomique pour insertion et mise à jour des soldes ---
    @Transaction
    suspend fun insertTransactionAndUpdateSoldes(
        transaction: TransactionEntity,
        soldeDao: SoldeDao
    ): Long {
        val (soldeAvant, soldeApres) = adjustSoldesForTransaction(
            transaction,
            soldeDao,
            undo = false
        )

        val generatedId = insertAndReturnId(
            transaction.copy(
                transactionCode = "",
                soldeAvant = soldeAvant,
                soldeApres = soldeApres,
                horodatage = System.currentTimeMillis()
            )
        )

        val codeTransaction =
            " ${String.format("%07d", generatedId)}${transaction.creePar}${transaction.codeAgence}"
        updateCodeTransaction(generatedId, codeTransaction)
        return generatedId
    }

    @Query("UPDATE flux_caisse SET isSynced=1 WHERE id=:id")
    suspend fun makeAsSync(id: Long)
}


private suspend fun adjustSoldesForTransaction(
    transaction: TransactionEntity,
    soldeDao: SoldeDao,
    undo: Boolean
): Pair<BigDecimal?, BigDecimal?> {
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
                codeAgence = transaction.codeAgence,
                isSynced = false
            )
    }

    suspend fun updateSoldeOrThrow(
        type: SoldeType,
        delta: BigDecimal,
        errorMessage: String
    ): Pair<BigDecimal, BigDecimal> {
        val soldeActuel = getOrCreateSolde(type)
        val soldeAvant = soldeActuel.montant
        val nouveauMontant = soldeAvant + delta
        if (nouveauMontant < BigDecimal.ZERO) {
            throw IllegalStateException(errorMessage)
        }
        soldeDao.updateMontant(idOp, type, devise, nouveauMontant)
        return soldeAvant to nouveauMontant
    }

    return when (transaction.type) {
        TransactionType.DEPOT -> {
            if (!undo) {
                updateSoldeOrThrow(
                    SoldeType.VIRTUEL,
                    -montant,
                    "Solde virtuel insuffisant pour effectuer un dépôt."
                )
                val (avant, apres) = updateSoldeOrThrow(
                    SoldeType.PHYSIQUE,
                    montant,
                    "Solde physique insuffisant pour effectuer un dépôt."
                )
                avant to apres
            } else {
                val (avant, apres) = updateSoldeOrThrow(
                    SoldeType.PHYSIQUE,
                    -montant,
                    "Solde physique insuffisant pour annuler ce dépôt."
                )
                updateSoldeOrThrow(
                    SoldeType.VIRTUEL,
                    montant,
                    "Solde virtuel insuffisant pour annuler ce dépôt."
                )
                avant to apres
            }
        }

        TransactionType.RETRAIT -> {
            if (!undo) {
                updateSoldeOrThrow(
                    SoldeType.VIRTUEL,
                    montant,
                    "Solde virtuel insuffisant pour effectuer un retrait."
                )
                val (avant, apres) = updateSoldeOrThrow(
                    SoldeType.PHYSIQUE,
                    -montant,
                    "Solde physique insuffisant pour effectuer un retrait."
                )
                avant to apres
            } else {
                val (avant, apres) = updateSoldeOrThrow(
                    SoldeType.PHYSIQUE,
                    montant,
                    "Solde physique insuffisant pour annuler ce retrait."
                )
                updateSoldeOrThrow(
                    SoldeType.VIRTUEL,
                    -montant,
                    "Solde virtuel insuffisant pour annuler ce retrait."
                )
                avant to apres
            }
        }

        TransactionType.TRANSFERT_ENTRANT -> {
            val delta = if (undo) -montant else montant
            val message = if (undo) {
                "Solde virtuel insuffisant pour annuler ce transfert entrant."
            } else {
                "Solde virtuel insuffisant pour effectuer un transfert entrant."
            }
            val (avant, apres) = updateSoldeOrThrow(SoldeType.VIRTUEL, delta, message)
            avant to apres
        }

        TransactionType.TRANSFERT_SORTANT -> {
            val delta = if (undo) montant else -montant
            val message = if (undo) {
                "Solde virtuel insuffisant pour annuler ce transfert sortant."
            } else {
                "Solde virtuel insuffisant pour le transfert sortant."
            }
            val (avant, apres) = updateSoldeOrThrow(SoldeType.VIRTUEL, delta, message)
            avant to apres
        }

        TransactionType.COMMISSION -> {
            val delta = if (undo) -montant else montant
            val message = if (undo) {
                "Solde physique insuffisant pour annuler cette commission."
            } else {
                "Solde physique insuffisant pour enregistrer cette commission."
            }
            val (avant, apres) = updateSoldeOrThrow(SoldeType.PHYSIQUE, delta, message)
            avant to apres
        }
    }
}
