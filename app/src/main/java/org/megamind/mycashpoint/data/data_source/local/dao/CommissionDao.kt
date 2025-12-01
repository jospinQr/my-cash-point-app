package org.megamind.mycashpoint.data.data_source.local.dao



import androidx.room.*
import kotlinx.coroutines.flow.Flow
import org.megamind.mycashpoint.data.data_source.local.entity.CommissionEntity
import org.megamind.mycashpoint.domain.model.TransactionType
import org.megamind.mycashpoint.utils.Constants

@Dao
interface CommissionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCommission(commission: CommissionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(commissions: List<CommissionEntity>)

    @Update
    suspend fun updateCommission(commission: CommissionEntity)
    @Delete
    suspend fun deleteCommission(commission: CommissionEntity)

    @Query("DELETE FROM commissions")
    suspend fun clearAll()


    @Query("SELECT * FROM commissions ORDER BY id DESC")
    fun getAllCommissions(): Flow<List<CommissionEntity>>

    @Query("SELECT * FROM commissions WHERE id = :id LIMIT 1")
    suspend fun getCommissionById(id: Long): CommissionEntity?


    @Query("""
        SELECT * FROM commissions 
        WHERE idOperateur = :idOperateur
        ORDER BY type
    """)
    fun getCommissionsByOperateur(idOperateur: Int): Flow<List<CommissionEntity>>

    @Query("""
        SELECT * FROM commissions 
        WHERE idOperateur = :idOperateur 
          AND type = :type 
          AND devise = :devise 
        LIMIT 1
    """)
    suspend fun getCommission(
        idOperateur: Int,
        type: TransactionType,
        devise: Constants.Devise
    ): CommissionEntity?



    @Query("""
        SELECT idOperateur, COUNT(*) AS nombreTaux, AVG(taux) AS tauxMoyen
        FROM commissions
        GROUP BY idOperateur
    """)
    fun getStatsParOperateur(): Flow<List<CommissionStats>>

    @Query("""
        SELECT devise, COUNT(*) AS nombreTaux, AVG(taux) AS tauxMoyen
        FROM commissions
        GROUP BY devise
    """)
    fun getStatsParDevise(): Flow<List<CommissionStats>>


    @Query("""
        SELECT * FROM commissions 
        WHERE idOperateur = :idOperateur 
          AND (type LIKE '%' || :query || '%' OR devise LIKE '%' || :query || '%')
    """)
    fun searchCommissions(idOperateur: Int, query: String): Flow<List<CommissionEntity>>
}
data class CommissionStats(
    val idOperateur: Int? = null,
    val devise: Constants.Devise? = null,
    val nombreTaux: Int,
    val tauxMoyen: Double
)

