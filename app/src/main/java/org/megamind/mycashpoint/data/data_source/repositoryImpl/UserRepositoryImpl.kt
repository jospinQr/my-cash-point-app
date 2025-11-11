package org.megamind.mycashpoint.data.data_source.repositoryImpl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.megamind.mycashpoint.data.data_source.local.dao.UserDao
import org.megamind.mycashpoint.data.data_source.local.mapper.toUser
import org.megamind.mycashpoint.data.data_source.local.mapper.toUserEntity
import org.megamind.mycashpoint.domain.model.User
import org.megamind.mycashpoint.domain.repository.UserRepository
import org.megamind.mycashpoint.utils.Result
import java.security.MessageDigest
import java.util.UUID

class UserRepositoryImpl(private val userDao: UserDao) : UserRepository {
    override fun login(
        userName: String,
        password: String,
        agenceId: String
    ): Flow<Result<User>> = flow {
        emit(Result.Loading)
        val user = userDao.getUserByEmail(userName)
        val hashedPassword = hashPassword(password, user?.toUser()?.salt ?: "")
        if (user != null && user.password == hashedPassword && user.codeAgence == agenceId) {
            emit(Result.Success(user.toUser()))
        } else {
            emit(Result.Error(Throwable("Email ou mot de passe incorrect")))
        }


    }

    override fun register(
        name: String,
        email: String,
        password: String,
        agenceId: String,
    ): Flow<Result<User>> = flow {

        emit(Result.Loading)

        try {
            val salt = UUID.randomUUID().toString()
            val hashedPassword = hashPassword(password, salt)
            val userEntity = User(0, name, email, hashedPassword, salt, agenceId).toUserEntity()
            userDao.insertUser(userEntity)
            emit(Result.Success(userEntity.toUser()))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }

    }

    override fun getUserByEmail(email: String): Flow<Result<User?>> = flow {

        try {

            emit(Result.Loading)
            val user = userDao.getUserByEmail(email)?.toUser()
            emit(Result.Success(user))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }


    }

    private fun hashPassword(password: String, salt: String): String {
        val md = MessageDigest.getInstance("SHA-256")
        val bytes = md.digest((password + salt).toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }

}