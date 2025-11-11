package org.megamind.mycashpoint.data.data_source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.megamind.mycashpoint.data.data_source.local.entity.SoldeEntity
import org.megamind.mycashpoint.domain.model.SoldeType
import org.megamind.mycashpoint.utils.Constants
import java.math.BigDecimal

@Dao
interface SoldeDao {

    @Query(
        """
        SELECT * FROM soldes 
        WHERE idOperateur = :idOperateur 
          AND soldeType = :type 
          AND devise = :devise
        LIMIT 1
    """
    )
    suspend fun getSoldeByOperateurEtTypeEtDevise(
        idOperateur: Int,
        type: SoldeType,
        devise: Constants.Devise
    ): SoldeEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(solde: SoldeEntity)


    @Query(
        """
        UPDATE soldes 
        SET montant = :nouveauMontant, 
            dernierMiseAJour = :dernierMiseAJour 
        WHERE idOperateur = :idOp 
          AND soldeType = :type 
          AND devise = :devise
    """
    )
    suspend fun updateMontant(
        idOp: Int,
        type: SoldeType,
        devise: Constants.Devise,
        nouveauMontant: BigDecimal,
        dernierMiseAJour: Long = System.currentTimeMillis()
    )

    @Query("SELECT * FROM soldes ORDER BY idOperateur")
    suspend fun getAll(): List<SoldeEntity>

    @Query("DELETE FROM soldes WHERE idOperateur = :idOperateur AND devise = :devise")
    suspend fun deleteByOperateurEtDevise(
        idOperateur: Int,
        devise: Constants.Devise
    )
}
