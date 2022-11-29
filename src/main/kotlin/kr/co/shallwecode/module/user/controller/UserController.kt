package kr.co.shallwecode.module.user.controller

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kr.co.shallwecode.module.user.serivce.LoginService
import kr.co.shallwecode.module.user.serivce.RegisterService
import org.kodein.di.instance
import org.kodein.di.ktor.controller.AbstractDIController
import java.util.*

class UserController(application: Application) : AbstractDIController(application) {
    private val loginService: LoginService by instance()
    private val registerService: RegisterService by instance()


    override fun Route.getRoutes() {
        val config = environment?.config ?: throw RuntimeException("ApplicationConfig 를 불러올 수 없습니다.")

        val secret = config.property("jwt.secret").getString()
        val issuer = config.property("jwt.issuer").getString()
        val exp = config.property("jwt.exp").getString().toLong()

        /*
        사용자 로그인
        */
        route("/login") {
            post {
                val request = call.receive<LoginRequest>()
                val foundUser = loginService.login(request)

                if (foundUser != null) {
                    val token = JWT.create()
                        .withIssuer(issuer)
                        .withClaim("userId", foundUser.id)
                        .withExpiresAt(Date(System.currentTimeMillis() + exp))
                        .sign(Algorithm.HMAC256(secret))
                    call.respond(
                        LoginResponse(
                            user = foundUser,
                            token = token
                        )
                    )
                } else {
                    call.respondText("login failed", status = HttpStatusCode.Unauthorized)
                }
            }
        }


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
    }

}