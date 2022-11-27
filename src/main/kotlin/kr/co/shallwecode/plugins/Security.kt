package kr.co.shallwecode.plugins

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.sessions.*
import kr.co.shallwecode.module.user.controller.LoginRequest
import kr.co.shallwecode.module.user.serivce.LoginService
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI


fun Application.configureSecurity() {
    val di = closestDI()

    val loginService by di.instance<LoginService>()

    authentication {
        session<UserSession>(AuthenticationConfigName.SWC_USER_SESSION.name) {
            validate { session ->
                val userModel = loginService.login(
                    LoginRequest(
                        loginId = session.loginId,
                        password = session.password
                    )
                )

                if (userModel != null) {
                    session
                } else {
                    null
                }
            }
        }
    }

    install(Sessions) {
        cookie<UserSession>(AuthenticationConfigName.SWC_USER_SESSION.name) {
            cookie.path = "/"
            cookie.maxAgeInSeconds = 60 * 60 * 24
            cookie.httpOnly = true

            // TODO 쿠키 도메인 설정 필요
        }
    }
}

data class UserSession(val loginId: String, val password: String) : Principal
enum class AuthenticationConfigName {
    SWC_USER_SESSION
}
