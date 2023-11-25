package shallwecode.kr

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import shallwecode.kr.database.DatabaseFactory
import shallwecode.kr.plugins.*
import javax.xml.crypto.Data

fun main(args: Array<String>): Unit = EngineMain.main(args)

fun Application.module() {
    configureSecurity()
    configureHTTP()
    configureSerialization()
//    configureDatabases()
    DatabaseFactory.init()
    configureRouting()
}
