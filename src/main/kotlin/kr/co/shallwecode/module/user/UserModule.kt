package kr.co.shallwecode.module.user


import io.ktor.server.application.*
import io.ktor.server.routing.*
import kr.co.shallwecode.module.database.dbModule
import kr.co.shallwecode.module.user.controller.UserController
import kr.co.shallwecode.module.user.persistence.repository.UserRepository
import kr.co.shallwecode.module.user.serivce.LoginService
import kr.co.shallwecode.module.user.serivce.RegisterService
import org.kodein.di.*
import org.kodein.di.ktor.controller.controller

val userModule = DI.Module(name = "userModule") {
    import(dbModule)
    bind { singleton { UserRepository(instance()) } }
    bind { singleton { LoginService(instance()) } }
    bind { singleton { RegisterService(instance()) } }
}

fun Application.userRouting() {
    routing {
        controller { UserController(instance()) }
    }
}