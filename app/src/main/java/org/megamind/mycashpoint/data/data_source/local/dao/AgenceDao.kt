package org.megamind.mycashpoint.data.data_source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.megamind.mycashpoint.data.data_source.local.entity.AgenceEntity

@Dao
interface AgenceDao {


    @Query("SELECT * FROM agence")
    suspend fun getAll(): List<AgenceEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(agence: AgenceEntity)

    @Query("DELETE FROM agence WHERE codeAgence = :id")
    suspend fun deleteById(id: String)

    @Query("SELECT * FROM agence WHERE codeAgence = :id")
    suspend fun getByID(id: String): AgenceEntity?


}