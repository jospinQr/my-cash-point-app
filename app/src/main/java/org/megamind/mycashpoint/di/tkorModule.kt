package org.megamind.mycashpoint.di

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.koin.dsl.module
import org.megamind.mycashpoint.data.data_source.remote.service.AuthService
import org.megamind.mycashpoint.data.data_source.remote.service.SoldeService
import org.megamind.mycashpoint.data.data_source.remote.service.TransactionService
import org.megamind.mycashpoint.ui.screen.main.utils.DataStorageManager

val ktorModule = module {

    single<HttpClient> {
        val dataStorageManager: DataStorageManager = get()
        HttpClient(OkHttp) {
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true

                })
            }
            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.ALL
            }

            defaultRequest {
                url("http://192.168.1.2:8084/api/v1/")
                header(HttpHeaders.ContentType, ContentType.Application.Json)
                header("Authorization", "Bearer ${runBlocking { dataStorageManager.getToken() }}")


            }
        }
    }




    single {
        AuthService(get())
    }

    single {
        TransactionService(get())
    }
    single {
        SoldeService(get())
    }

}

