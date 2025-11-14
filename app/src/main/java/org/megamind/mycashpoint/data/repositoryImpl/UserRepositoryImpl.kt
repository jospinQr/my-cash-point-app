package org.megamind.mycashpoint.data.repositoryImpl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.megamind.mycashpoint.data.data_source.local.dao.UserDao
import org.megamind.mycashpoint.data.data_source.local.entity.UserEntity
import org.megamind.mycashpoint.data.data_source.local.mapper.toUser
import org.megamind.mycashpoint.data.data_source.remote.dto.AuthResponse
import org.megamind.mycashpoint.data.data_source.remote.dto.LoginRequest
import org.megamind.mycashpoint.data.data_source.remote.dto.RegisterRequest
import org.megamind.mycashpoint.data.data_source.remote.service.AuthService
import org.megamind.mycashpoint.domain.model.User
import org.megamind.mycashpoint.domain.repository.UserRepository
import org.megamind.mycashpoint.utils.DataStorageManager
import org.megamind.mycashpoint.utils.Result
import org.megamind.mycashpoint.utils.decodeJwtPayload

class UserRepositoryImpl(
    private val userDao: UserDao,
    private val authService: AuthService,
    private val dataStorageManager: DataStorageManager // pour sauvegarder token & infos user
) : UserRepository {

    override fun login(loginRequest: LoginRequest): Flow<Result<AuthResponse>> = flow {
        emit(Result.Loading)
        try {
            val result = authService.login(loginRequest)

            result.data?.token?.let {
                dataStorageManager.saveToken(it)

                val claims = decodeJwtPayload(it)
                val userEntity = UserEntity(
                    id = claims.optString("sub").toLong(),
                    name = claims.optString("name"),
                    codeAgence = claims.optString("agence_code"),
                    role = claims.optString("role")
                )

                userDao.insertUser(userEntity)

            }

            emit(Result.Success(result.data))
        } catch (ex: Exception) {
            emit(Result.Error(ex))
        }
    }

    override fun register(registerRequest: RegisterRequest): Flow<Result<AuthResponse>> = flow {
        emit(Result.Loading)
        try {

            val result = authService.register(registerRequest)
            emit(Result.Success(result.data))
        } catch (ex: Exception) {
            emit(Result.Error(ex))

        }
    }

    override fun getUserById(id: Long): Flow<Result<User?>> = flow {
        emit(Result.Loading)
        try {
            val user = userDao.getUserById(id)?.toUser()
            emit(Result.Success(user))
        } catch (ex: Exception) {
            emit(Result.Error(ex))
        }
    }
}
