package kr.co.shallwecode.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*


fun Application.configureSecurity() {

    authentication {
        jwt(AuthenticationConfigName.AUTH_JWT.name) {
            val secret = this@configureSecurity.environment.config.property("jwt.secret").getString()
            val issuer = this@configureSecurity.environment.config.property("jwt.issuer").getString()
            verifier(
                JWT
                    .require(Algorithm.HMAC256(secret))
                    .withIssuer(issuer)
                    .acceptExpiresAt(0)
                    .build()
            )

//            validate { credential ->
//                if (credential.payload.getClaim("userId").asString() != "") {
//                    JWTPrincipal(credential.payload)
//                } else {
//                    null
//                }
//            }
//
//            challenge { _, _ ->
//                call.respond(HttpStatusCode.Unauthorized, "Token is not valid or has expired")
//            }
        }
    }

//
//    authentication {
//        session<UserSession>(AuthenticationConfigName.SWC_USER_SESSION.name) {
//            validate { session ->
//                val userModel = loginService.login(
//                    LoginRequest(
//                        loginId = session.loginId,
//                        password = session.password
//                    )
//                )
//
//                if (userModel != null) {
//                    session
//                } else {
//                    null
//                }
//            }
//        }
//    }
//
//    install(Sessions) {
//        cookie<UserSession>(AuthenticationConfigName.SWC_USER_SESSION.name) {
//            cookie.path = "/"
//            cookie.maxAgeInSeconds = 60 * 60 * 24
////            cookie.httpOnly = true
//            // TODO 쿠키 도메인 설정 필요
//            cookie.domain = "localhost"
//        }
//    }
}

//data class UserSession(val loginId: String, val password: String) : Principal
enum class AuthenticationConfigName {
    AUTH_JWT
}
