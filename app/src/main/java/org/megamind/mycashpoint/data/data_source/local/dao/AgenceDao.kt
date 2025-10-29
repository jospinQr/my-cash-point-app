package org.megamind.mycashpoint.data.data_source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.megamind.mycashpoint.data.data_source.local.entity.Agence

@Dao
interface AgenceDao {


    @Query("SELECT * FROM agence")
    suspend fun getAll(): List<Agence>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(agence: Agence)

    @Query("DELETE FROM agence WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("SELECT * FROM agence WHERE id = :id")
    suspend fun getByID(id: String): Agence?


}