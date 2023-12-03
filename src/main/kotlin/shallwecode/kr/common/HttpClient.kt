package shallwecode.kr.common

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.cookies.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual

val API_CLIENT = HttpClient(CIO) {
    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
            encodeDefaults = true
            prettyPrint = true
            serializersModule = SerializersModule {
                contextual(LocalDateSerializer)
                contextual(LocalDateTimeSerializer)
            }
        })
    }
//    install(HttpTimeout) {
//        requestTimeoutMillis = 1000
//    }
}
