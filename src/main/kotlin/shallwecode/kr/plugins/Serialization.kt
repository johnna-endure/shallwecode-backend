package shallwecode.kr.plugins

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import shallwecode.kr.common.LocalDateSerializer
import shallwecode.kr.common.LocalDateTimeSerializer

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
            prettyPrint = true
            serializersModule = SerializersModule {
                contextual(LocalDateSerializer)
                contextual(LocalDateTimeSerializer)
            }
        })

    }
}

