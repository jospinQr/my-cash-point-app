package org.megamind.mycashpoint.data.data_source.remote


import android.util.Log
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import kotlinx.io.IOException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.megamind.mycashpoint.ui.screen.main.utils.Result


suspend inline fun <reified T> safeApiCall(
    crossinline block: suspend () -> HttpResponse
): Result<T> {
    return try {
        val response = block()

        if (response.status.isSuccess()) {
            Log.i("SafeApi", response.toString())
            Result.Success(response.body<T>())

        } else {
            val errorBody = response.bodyAsText()
            Result.Error(
                ApiException(
                    code = response.status.value,
                    message = extractMessageFromErrorBody(errorBody) ?: response.status.description,
                    errorBody = errorBody
                )
            )
        }
    } catch (e: IOException) {


        Log.e("safeApiCall", e.message.toString())
        Result.Error(NetworkException("Vérifiez votre connexion au serveur", e))

    } catch (e: ApiException) {
        Result.Error(e)
    } catch (e: Exception) {
        Result.Error(UnknownApiException(e))
    }
}

// Si ton API renvoie du JSON avec un champ "message"
fun extractMessageFromErrorBody(body: String): String? {
    return try {
        val json = Json.parseToJsonElement(body).jsonObject
        json["error"]?.jsonPrimitive?.content
    } catch (_: Exception) {
        null
    }
}


class NetworkException(
    override val message: String,
    override val cause: Throwable? = null
) : Exception(message, cause)


class ApiException(
    val code: Int,
    override val message: String,
    val errorBody: String? = null
) : Exception("Erreur API $message $errorBody $code")

class UnknownApiException(
    override val cause: Throwable? = null
) : Exception("Erreur inconnue lors de l'appel API $cause", cause)


sealed class AppError {
    data class NetworkError(val message: String) : AppError()
    data class ApiError(val message: String) : AppError()
    data class UnknownError(val message: String) : AppError()
}