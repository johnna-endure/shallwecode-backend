package kr.co.shallwecode

import com.typesafe.config.ConfigFactory
import io.ktor.server.application.*
import io.ktor.server.config.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kr.co.shallwecode.module.user.userModule
import kr.co.shallwecode.module.user.userRouting
import kr.co.shallwecode.plugins.configureHTTP
import kr.co.shallwecode.plugins.configureSecurity
import kr.co.shallwecode.plugins.configureSerialization
import org.kodein.di.ktor.di

fun main() {
//    embeddedServer(Netty, port = 8080) {
//        di {
//            import(userModule)
//        }
//
//        configureHTTP()
//        configureSecurity()
//        configureSerialization()
//        configureRouting()
//    }.start(wait = true)
    embeddedServer(Netty, environment = applicationEngineEnvironment {
        config = HoconApplicationConfig(ConfigFactory.load())

        module {
            di {
                import(userModule)
            }

            configureHTTP()
            configureSecurity()
            configureSerialization()
            configureRouting()
        }


        connector {
            port = 8080
            host = "127.0.0.1"
        }
    }).start(wait = true)
}

fun Application.configureRouting() {
    userRouting()
}
