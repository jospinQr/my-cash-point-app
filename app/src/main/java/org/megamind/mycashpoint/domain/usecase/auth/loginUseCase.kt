package org.megamind.mycashpoint.domain.usecase.auth

import kotlinx.coroutines.flow.Flow
import org.megamind.mycashpoint.data.data_source.remote.dto.AuthResponse
import org.megamind.mycashpoint.data.data_source.remote.dto.LoginRequest
import org.megamind.mycashpoint.domain.repository.UserRepository
import org.megamind.mycashpoint.utils.Result

class LoginUseCase(private val userRepository: UserRepository) {

    operator fun invoke(email: String, password: String): Flow<Result<AuthResponse>> {

        val loginRequest = LoginRequest(email, password)
        return userRepository.login(loginRequest)

    }


}