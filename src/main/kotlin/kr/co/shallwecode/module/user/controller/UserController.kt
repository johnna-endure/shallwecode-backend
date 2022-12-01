package kr.co.shallwecode.module.user.controller

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import kr.co.shallwecode.module.user.constant.AuthenticationSource
import kr.co.shallwecode.module.user.serivce.AuthenticationService
import kr.co.shallwecode.module.user.serivce.UserService
import org.kodein.di.instance
import org.kodein.di.ktor.controller.AbstractDIController

class UserController(application: Application) : AbstractDIController(application) {
    private val authenticationService: AuthenticationService by instance()
    private val userService: UserService by instance()


    override fun Route.getRoutes() {
        // 사용자 정보 저장
        route("/user") {
            post {
                val request = call.receive<UserCreateRequest>()
                // 사용자 정보 저장
                val createdUser = userService.createUser(request)
                if (createdUser != null) {
                    // 인증 정보 저장
                    authenticationService.createAuthInfo(
                        AuthInfoCreateRequest(
                            userId = createdUser.id,
                            from = AuthenticationSource.SHALL_WE_CODE.name,
                            loginId = request.loginId,
                            password = request.password,
                        )
                    )
                    call.respond(createdUser)
                } else {
                    call.respondText("created failed", status = HttpStatusCode.InternalServerError)
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
data class UserCreateRequest(
    val password: String,
    val email: String,
    val loginId: String,
    val name: String? = null,
    val blogUrl: String? = null,
    val githubUrl: String? = null
)

