package org.megamind.mycashpoint.data.data_source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity()
data class Agence(

    @PrimaryKey
    val id: String,
    val designation: String,

)
