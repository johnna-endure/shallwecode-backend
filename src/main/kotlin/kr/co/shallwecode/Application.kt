package kr.co.shallwecode

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kr.co.shallwecode.module.user.userModule
import kr.co.shallwecode.module.user.userRouting
import kr.co.shallwecode.plugins.configureSecurity
import kr.co.shallwecode.plugins.configureSerialization
import org.kodein.di.ktor.di

fun main() {
    embeddedServer(Netty, port = 8080) {

        di {
            import(userModule)
        }

        configureSecurity()
        configureSerialization()
        configureRouting()
    }.start(wait = true)
}

fun Application.configureRouting() {
    userRouting()
}
