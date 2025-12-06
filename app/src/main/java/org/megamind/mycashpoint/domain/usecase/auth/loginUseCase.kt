package org.megamind.mycashpoint.domain.usecase.auth

import kotlinx.coroutines.flow.Flow
import org.megamind.mycashpoint.data.data_source.remote.dto.auth.AuthResponse
import org.megamind.mycashpoint.data.data_source.remote.dto.auth.LoginRequest
import org.megamind.mycashpoint.domain.repository.UserRepository
import org.megamind.mycashpoint.utils.Result

class LoginUseCase(private val userRepository: UserRepository) {


    operator fun invoke(userName: String, password: String): Flow<Result<AuthResponse>> {

        val loginRequest = LoginRequest(userName, password)
        return userRepository.login(loginRequest)

    }


}