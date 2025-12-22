package org.megamind.mycashpoint.data.data_source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.megamind.mycashpoint.data.data_source.local.entity.EtablissementEntity

@Dao
interface EtablissementDao {

    @Query("SELECT * FROM etablissement WHERE id = 1")
    suspend fun getEtablissement(): EtablissementEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEtablissement(etablissement: EtablissementEntity)


}