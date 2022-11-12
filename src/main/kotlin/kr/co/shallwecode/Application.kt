package kr.co.shallwecode

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kr.co.shallwecode.persistence.DatabaseFactory
import kr.co.shallwecode.plugins.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
//    configureSecurity()
    DatabaseFactory.init()
    configureHTTP()
    configureSerialization()
    configureRouting()
}
