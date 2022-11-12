package kr.co.shallwecode.routes.user

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kr.co.shallwecode.persistence.user.dao.UserDao

fun Route.userRouting() {

    val userDao = UserDao()

    route("/user") {
        post {
            val request = call.receive<UserCreateRequest>()
            val createdUser = userDao.createUser(request)
            if (createdUser != null) {
                call.respond(createdUser)
            } else {
                call.respondText("created failed", status = HttpStatusCode .InternalServerError)
            }
        }
    }

    route("/login") {
        post {
            val request = call.receive<LoginRequest>()
            val foundUser = userDao.findUser(request.loginId, request.password)

            if (foundUser != null) {
                call.respond(foundUser)
            } else {
                call.respondText ("login failed", status = HttpStatusCode.Unauthorized)
            }
        }
    }
}
