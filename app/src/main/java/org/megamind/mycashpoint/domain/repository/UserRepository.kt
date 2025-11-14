package org.megamind.mycashpoint.domain.repository

import kotlinx.coroutines.flow.Flow
import org.megamind.mycashpoint.data.data_source.local.entity.UserEntity
import org.megamind.mycashpoint.data.data_source.remote.dto.AuthResponse
import org.megamind.mycashpoint.data.data_source.remote.dto.LoginRequest
import org.megamind.mycashpoint.data.data_source.remote.dto.RegisterRequest
import org.megamind.mycashpoint.domain.model.User
import org.megamind.mycashpoint.utils.Result

interface UserRepository {

    fun login(loginRequest: LoginRequest): Flow<Result<AuthResponse>>

    fun register(
        registerRequest: RegisterRequest
    ): Flow<Result<AuthResponse>>

    fun getUserById(id: Long): Flow<Result<User?>>


}