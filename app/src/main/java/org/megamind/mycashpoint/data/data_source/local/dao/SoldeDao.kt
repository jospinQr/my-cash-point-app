package org.megamind.mycashpoint.data.data_source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.megamind.mycashpoint.data.data_source.local.entity.SoldeEntity
import org.megamind.mycashpoint.utils.Constants

@Dao
interface SoldeDao {

    @Query(
        """
        SELECT * FROM soldes 
        WHERE idOperateur = :idOperateur AND devise = :devise 
        LIMIT 1
        """
    )
    suspend fun getSoldeByOperateurEtDevise(
        idOperateur: Int,
        devise: String
    ): SoldeEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(solde: SoldeEntity)


    @Query(
        """
        UPDATE soldes 
        SET montant = :montant, dernierMiseAJour = :timestamp 
        WHERE idOperateur = :idOperateur AND devise = :devise
        """
    )
    suspend fun updateMontant(
        idOperateur: Int,
        devise: Constants.Devise,
        montant: Double,
        timestamp: Long = System.currentTimeMillis()
    )

    @Query("SELECT * FROM soldes ORDER BY idOperateur")
    suspend fun getAll(): List<SoldeEntity>

    @Query("DELETE FROM soldes WHERE idOperateur = :idOperateur AND devise = :devise")
    suspend fun deleteByOperateurEtDevise(
        idOperateur: Int,
        devise: Constants.Devise
    )
}
