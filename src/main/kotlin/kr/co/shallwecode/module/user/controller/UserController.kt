package kr.co.shallwecode.module.user.controller

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kr.co.shallwecode.module.user.persistence.repository.UserRepository
import kr.co.shallwecode.module.user.serivce.LoginService
import kr.co.shallwecode.module.user.serivce.RegisterService
import org.kodein.di.instance
import org.kodein.di.ktor.controller.AbstractDIController

class UserController(application: Application) : AbstractDIController(application) {
    private val loginService: LoginService by instance()
    private val registerService: RegisterService by instance()

    override fun Route.getRoutes() {
        // 사용자 정보 저장
        route("/user") {
            post {
                val request = call.receive<UserCreateRequest>()
                val createdUser = registerService.register(request)
                if (createdUser != null) {
                    call.respond(createdUser)
                } else {
                    call.respondText("created failed", status = HttpStatusCode.InternalServerError)
                }
            }
        }

        /*
        사용자 로그인
        basic auth 방식 - 세션 추가 필요
         */
        route("/login") {
            post {
                val request = call.receive<LoginRequest>()
                val foundUser = loginService.login(request)

                if (foundUser != null) {
                    call.respond(foundUser)
                } else {
                    call.respondText("login failed", status = HttpStatusCode.Unauthorized)
                }
            }
        }

    }

}