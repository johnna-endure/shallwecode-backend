package kr.co.shallwecode.module.user


import io.ktor.server.application.*
import io.ktor.server.routing.*
import kr.co.shallwecode.module.database.dbModule
import kr.co.shallwecode.module.user.controller.UserController
import kr.co.shallwecode.module.user.persistence.repository.UserRepository
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.ktor.controller.controller
import org.kodein.di.singleton

val userModule = DI.Module(name = "userModule") {
    import(dbModule)
    bind { singleton { UserRepository(instance()) } }
}

fun Application.userRouting() {
    routing {
        controller { UserController(instance()) }
    }
}