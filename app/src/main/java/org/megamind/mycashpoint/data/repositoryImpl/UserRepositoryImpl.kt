package org.megamind.mycashpoint.data.repositoryImpl

import android.util.Log
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
            when (val result = authService.login(loginRequest)) {
                Result.Loading -> Unit
                is Result.Success -> {

                    val token = result.data?.token
                    if (!token.isNullOrBlank()) {
                        dataStorageManager.saveToken(token)
                        val claims = decodeJwtPayload(token)
                        val userEntity = UserEntity(
                            id = claims.optString("sub").toLong(),
                            name = claims.optString("name"),
                            codeAgence = claims.optString("agence_code"),
                            role = claims.optString("role")
                        )
                        userDao.insertUser(userEntity)
                        emit(Result.Success(result.data))

                    } else {
                        emit(Result.Error(Exception("Token is null or blank")))
                    }
                }

                is Result.Error<*> -> {
                    emit(Result.Error(Exception(result.e?.message)))
                    Log.e("UserRepositoryImpl", "login: ${result.e}")

                }

            }

        } catch (ex: Exception) {
            emit(Result.Error(ex))
            Log.e("UserRepositoryImpl", "login: ${ex.message}")
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
