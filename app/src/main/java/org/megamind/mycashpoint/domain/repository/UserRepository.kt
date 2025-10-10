package org.megamind.mycashpoint.domain.repository

import kotlinx.coroutines.flow.Flow
import org.megamind.mycashpoint.data.data_source.local.entity.User
import org.megamind.mycashpoint.utils.Result

interface UserRepository {

    suspend fun login(userName: String, password: String): Flow<Result<User>>

    suspend fun register(name: String, email: String, password: String): Flow<Result<User>>

    suspend fun getUserByEmail(email: String): Flow<Result<User?>>


}