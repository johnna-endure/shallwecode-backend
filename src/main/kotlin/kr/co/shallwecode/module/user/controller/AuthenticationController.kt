package kr.co.shallwecode.module.user.controller

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import kr.co.shallwecode.module.user.persistence.table.UserModel
import kr.co.shallwecode.module.user.serivce.AuthenticationService
import kr.co.shallwecode.module.user.serivce.UserService
import org.kodein.di.instance
import org.kodein.di.ktor.controller.AbstractDIController
import java.util.*

class AuthenticationController(application: Application) : AbstractDIController(application) {
    private val authenticationService: AuthenticationService by instance()
    private val userService: UserService by instance()


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
                // 인증 정보 조회
                val authInfo = authenticationService.login(request)

                // 사용자 정보 조회
                val userId =
                    authInfo?.userId ?: return@post call.respond(HttpStatusCode.Unauthorized, "인증 정보를 찾을 수 없습니다.")
                val userInfo =
                    userService.find(userId) ?: return@post call.respond(
                        HttpStatusCode.Unauthorized,
                        "사용자 정보를 찾을 수 없습니다."
                    )

                // 토큰 생성
                val token = JWT.create()
                    .withIssuer(issuer)
                    .withClaim("userId", userInfo.id)
                    .withExpiresAt(Date(System.currentTimeMillis() + exp))
                    .sign(Algorithm.HMAC256(secret))

                call.respond(
                    LoginResponse(
                        user = userInfo,
                        token = token
                    )
                )
            }
        }
    }
}

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
