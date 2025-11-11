package org.megamind.mycashpoint.domain.repository

import kotlinx.coroutines.flow.Flow
import org.megamind.mycashpoint.data.data_source.local.entity.UserEntity
import org.megamind.mycashpoint.domain.model.User
import org.megamind.mycashpoint.utils.Result

interface UserRepository {

    fun login(userName: String, password: String, agenceId: String): Flow<Result<User>>

    fun register(
        name: String,
        email: String,
        password: String,
        agenceId: String
    ): Flow<Result<User>>

    fun getUserByEmail(email: String): Flow<Result<User?>>


}