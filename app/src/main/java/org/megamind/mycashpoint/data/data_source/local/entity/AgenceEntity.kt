package org.megamind.mycashpoint.data.data_source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "agence")
data class AgenceEntity(

    @PrimaryKey
    val codeAgence: String,
    val designation: String,

    )
