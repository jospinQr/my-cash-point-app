package org.megamind.mycashpoint.data.data_source.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(
    tableName = "etablissement", indices = [Index(value = ["id"])]
)
data class EtablissementEntity(

    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val addrees: String,
    val contat: String? = null,
    val rccm: String? = null

)