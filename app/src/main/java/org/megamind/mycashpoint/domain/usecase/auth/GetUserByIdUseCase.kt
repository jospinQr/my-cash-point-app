package org.megamind.mycashpoint.domain.usecase.auth

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.megamind.mycashpoint.domain.model.User
import org.megamind.mycashpoint.domain.repository.UserRepository
import org.megamind.mycashpoint.utils.Result
import org.megamind.mycashpoint.utils.Result.*

class GetUserByIdUseCase(private val userRepository: UserRepository) {


    operator fun invoke(userId: Long): Flow<Result<User?>> = userRepository.getUserById(userId)


}