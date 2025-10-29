package org.megamind.mycashpoint.data.data_source.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "users", foreignKeys = [
        ForeignKey(
            entity = Agence::class,
            parentColumns = ["id"],
            childColumns = ["idAgence"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class User(

    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val email: String,
    val password: String,
    val salt: String,
    val idAgence: String,

    )