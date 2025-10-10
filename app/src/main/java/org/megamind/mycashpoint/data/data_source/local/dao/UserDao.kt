package org.megamind.mycashpoint.data.data_source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import org.megamind.mycashpoint.data.data_source.local.entity.User

@Dao
interface UserDao {


    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM users WHERE email = :email")
    suspend fun getUserByEmail(email: String): User?


    @Query("SELECT * FROM users WHERE id = :id")
    suspend fun getUserById(id: Int): User?




}