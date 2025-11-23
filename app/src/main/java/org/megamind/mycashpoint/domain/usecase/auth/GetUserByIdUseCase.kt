package org.megamind.mycashpoint.domain.usecase.auth

import kotlinx.coroutines.flow.Flow
import org.megamind.mycashpoint.domain.model.User
import org.megamind.mycashpoint.domain.repository.UserRepository
import org.megamind.mycashpoint.ui.screen.main.utils.Result

class GetUserByIdUseCase(private val userRepository: UserRepository) {


    operator fun invoke(userId: Long): Flow<Result<User?>> = userRepository.getUserById(userId)


}