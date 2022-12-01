package kr.co.shallwecode.module.user


import io.ktor.server.application.*
import io.ktor.server.routing.*
import kr.co.shallwecode.module.database.dbModule
import kr.co.shallwecode.module.user.controller.AuthenticationController
import kr.co.shallwecode.module.user.controller.UserController
import kr.co.shallwecode.module.user.persistence.repository.AuthenticationRepository
import kr.co.shallwecode.module.user.persistence.repository.UserRepository
import kr.co.shallwecode.module.user.serivce.AuthenticationService
import kr.co.shallwecode.module.user.serivce.UserService
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.ktor.controller.controller
import org.kodein.di.singleton

val userModule = DI.Module(name = "userModule") {
    import(dbModule)
    bind { singleton { UserRepository(instance()) } }
    bind { singleton { AuthenticationRepository(instance()) } }
    
    bind { singleton { AuthenticationService(instance()) } }
    bind { singleton { UserService(instance()) } }
}

fun Application.userRouting() {
    routing {
        controller { UserController(instance()) }
        controller { AuthenticationController(instance()) }
    }
}