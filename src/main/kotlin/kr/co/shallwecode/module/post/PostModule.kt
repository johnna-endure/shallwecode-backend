package kr.co.shallwecode.module.post

import io.ktor.server.application.*
import io.ktor.server.routing.*
import kr.co.shallwecode.module.database.dbModule
import kr.co.shallwecode.module.post.controller.PostController
import kr.co.shallwecode.module.post.service.PostService
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.ktor.controller.controller
import org.kodein.di.singleton

val postModule = DI.Module(name = "postModule") {
    import(dbModule)
    bind { singleton { PostService(instance()) } }
}

fun Application.postRouting() {
    routing {
        controller { PostController(instance()) }
    }
}