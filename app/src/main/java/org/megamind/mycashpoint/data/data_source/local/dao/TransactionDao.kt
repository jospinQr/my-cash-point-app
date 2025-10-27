package org.megamind.mycashpoint.data.data_source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import org.megamind.mycashpoint.data.data_source.local.entity.SoldeEntity
import org.megamind.mycashpoint.data.data_source.local.entity.StatutSync
import org.megamind.mycashpoint.data.data_source.local.entity.TransactionEntity
import org.megamind.mycashpoint.utils.Constants

@Dao
interface TransactionDao {

    // --- CRUD simples ---

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(transaction: TransactionEntity)

    @Query("SELECT * FROM flux_caisse ORDER BY horodatage DESC")
    suspend fun getAll(): List<TransactionEntity>

    @Query(
        """
        SELECT * FROM flux_caisse 
        WHERE idOperateur = :idOperateur 
        AND device = :device 
        ORDER BY horodatage DESC
    """
    )
    suspend fun getByOperateurEtDevice(
        idOperateur: String,
        device: Constants.Devise
    ): List<TransactionEntity>

    @Query("SELECT * FROM flux_caisse WHERE statutSync = :statut")
    suspend fun getBySyncStatus(statut: StatutSync): List<TransactionEntity>

    @Query("DELETE FROM flux_caisse WHERE id = :id")
    suspend fun deleteById(id: String)


    // --- MÉTHODE MÉTIER ATOMIQUE ---
    /**
     * Insère une transaction et met à jour le solde correspondant.
     * L'opération est atomique : si l'un échoue, rien n'est appliqué.
     */
    @Transaction
    suspend fun ajouterTransactionEtMettreAJourSolde(
        transaction: TransactionEntity,
        soldeDao: SoldeDao
    ) {
        val devise = transaction.device

        // 1️⃣ Récupération du solde existant
        val soldeActuel = soldeDao.getSoldeByOperateurEtDevise(transaction.idOperateur, devise)
            ?: SoldeEntity(
                idOperateur = transaction.idOperateur,
                montant = 0L,
                devise = devise,
                dernierMiseAJour = System.currentTimeMillis()
            )

        // 2️⃣ Calcul du nouveau solde
        val nouveauSolde = soldeActuel.montant + transaction.montantSigne()

        // 3️⃣ Mise à jour de la transaction (solde avant/après)
        val transactionMaj = transaction.copy(
            soldeAvant = soldeActuel.montant,
            soldeApres = nouveauSolde
        )

        // 4️⃣ Insertion de la transaction
        insert(transactionMaj)

        // 5️⃣ Mise à jour du solde
        soldeDao.updateMontant(transaction.idOperateur, devise, nouveauSolde)
    }
}
