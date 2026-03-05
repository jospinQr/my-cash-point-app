package org.megamind.mycashpoint.data.data_source.remote.service

import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.header
import io.ktor.client.utils.EmptyContent.headers
import io.ktor.serialization.kotlinx.json.*
import io.ktor.http.HttpHeaders
import org.megamind.mycashpoint.data.data_source.remote.dto.auth.AuthResponse
import org.megamind.mycashpoint.data.data_source.remote.dto.auth.EditUserRequeste
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


    suspend fun editInfo(editInfoRequest: EditUserRequeste, token: String): Result<AuthResponse> {
        return safeApiCall<AuthResponse> {
            httpClient.put("auth/change-password") {

                setBody(editInfoRequest)
                header(HttpHeaders.Authorization, "Bearer $token")

            }
        }

    }


}