package org.megamind.mycashpoint.domain.usecase.auth

import kotlinx.coroutines.flow.Flow
import org.megamind.mycashpoint.data.data_source.remote.dto.auth.AuthResponse
import org.megamind.mycashpoint.data.data_source.remote.dto.auth.RegisterRequest
import org.megamind.mycashpoint.data.data_source.remote.dto.auth.Role
import org.megamind.mycashpoint.domain.repository.UserRepository
import org.megamind.mycashpoint.utils.Result

class RegisterUseCase(private val userRepository: UserRepository) {


    operator fun invoke(
        userName: String,
        password: String,
        agenceId: String,
        role: String
    ): Flow<Result<AuthResponse>> {

        val registerRequest = RegisterRequest(userName, password, agenceId, Role.valueOf(role))
        return userRepository.register(registerRequest)
    }


}