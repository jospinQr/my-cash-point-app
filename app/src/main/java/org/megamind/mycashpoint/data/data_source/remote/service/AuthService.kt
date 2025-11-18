package org.megamind.mycashpoint.data.data_source.remote.service

import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import org.megamind.mycashpoint.data.data_source.remote.dto.auth.AuthResponse
import org.megamind.mycashpoint.data.data_source.remote.dto.auth.LoginRequest
import org.megamind.mycashpoint.data.data_source.remote.dto.auth.RegisterRequest
import org.megamind.mycashpoint.data.data_source.remote.safeApiCall
import org.megamind.mycashpoint.utils.Result

class AuthService(private val httpClient: HttpClient) {

    suspend fun login(loginRequest: LoginRequest): Result<AuthResponse> {
        return safeApiCall<AuthResponse> {
            httpClient.post("auth/login") {
                setBody(loginRequest)
            }
        }
    }


    suspend fun register(registerRequest: RegisterRequest): Result<AuthResponse> {
        return safeApiCall<AuthResponse> {
            httpClient.post("auth/register") {
                setBody(registerRequest)
            }
        }

    }

}