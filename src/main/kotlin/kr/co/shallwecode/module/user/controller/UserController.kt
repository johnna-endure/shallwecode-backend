package kr.co.shallwecode.module.user.controller

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import kr.co.shallwecode.module.user.serivce.UserService
import kr.co.shallwecode.module.user.table.UserModel
import org.kodein.di.instance
import org.kodein.di.ktor.controller.AbstractDIController
import java.util.*

class UserController(application: Application) : AbstractDIController(application) {
    private val userService: UserService by instance()


    override fun Route.getRoutes() {
        val config = environment?.config ?: throw RuntimeException("ApplicationConfig 를 불러올 수 없습니다.")

        val secret = config.property("jwt.secret").getString()
        val issuer = config.property("jwt.issuer").getString()
        val exp = config.property("jwt.exp").getString().toLong()

        route("/login") {
            post {
                val request = call.receive<LoginRequest>()
                try {
                    val user = userService.login(request.loginId, request.password)
                    call.respond(
                        LoginResponse(
                            user = user,
                            token = JWT.create()
                                .withIssuer(issuer)
                                .withClaim("userId", user.id)
                                .withExpiresAt(Date(System.currentTimeMillis() + exp))
                                .sign(Algorithm.HMAC256(secret))
                        )
                    )
                } catch (ex: Exception) {
                    call.application.environment.log.debug("login failed : ${ex.stackTraceToString()}")
                    call.respond(HttpStatusCode.Unauthorized, "login failed")
                }
            }
        }

        // 사용자 정보 저장
        route("/user") {
            post {
                val request = call.receive<UserRegisterRequest>()

                try {
                    val userId = userService.register(request)
                    call.respond(userId)
                } catch (ex: Exception) {
                    call.application.environment.log.error("register failed. ${ex.stackTraceToString()}")
                    call.respond(HttpStatusCode.InternalServerError)
                    return@post
                }
            }
        }

        // 테스트용 api
        route("/test") {
            get {
                call.respondText("hello world!")
            }
        }
    }

}

@Serializable
data class UserRegisterRequest(
    val password: String,
    val email: String,
    val loginId: String,
    val name: String? = null,
    val blogUrl: String? = null,
    val githubUrl: String? = null
)


@Serializable
data class LoginRequest(
    val loginId: String,
    val password: String
)


@Serializable
data class AuthInfoCreateRequest(
    val userId: Long,
    var from: String? = null,
    val loginId: String,
    val password: String
)


@Serializable
data class LoginResponse(val user: UserModel, val token: String)

