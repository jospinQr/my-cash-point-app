package org.megamind.mycashpoint.domain.usecase.auth

import kotlinx.coroutines.flow.Flow
import org.megamind.mycashpoint.data.data_source.remote.dto.auth.AuthResponse
import org.megamind.mycashpoint.data.data_source.remote.dto.auth.EditUserRequeste
import org.megamind.mycashpoint.domain.repository.UserRepository
import org.megamind.mycashpoint.utils.Result

class EditUserNameOrPasswordUseCase(private val userRepository: UserRepository) {

    operator fun invoke(
        token: String,
        editUserRequeste: EditUserRequeste
    ): Flow<Result<AuthResponse>> {
        return userRepository.editInfo(token, editUserRequeste)
    }
}