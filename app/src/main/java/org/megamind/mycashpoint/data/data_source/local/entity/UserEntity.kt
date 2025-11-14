package org.megamind.mycashpoint.data.data_source.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "users",
)
data class UserEntity(

    @PrimaryKey()
    val id: Long,
    val name: String,
    val codeAgence: String,
    val role: String,

    )