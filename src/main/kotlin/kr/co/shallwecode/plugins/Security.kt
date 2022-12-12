package kr.co.shallwecode.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*


fun Application.configureSecurity() {
    val secret = environment.config.property("jwt.secret").getString()
    val issuer = environment.config.property("jwt.issuer").getString()

    authentication {
        jwt(AuthenticateName.AUTH_JWT.name) {
            verifier(
                JWT
                    .require(Algorithm.HMAC256(secret))
                    .withIssuer(issuer)
                    .acceptExpiresAt(0)
                    .build()
            )
            // TODO userId 있는지 확인
            validate { jwtCredential ->
                JWTPrincipal(jwtCredential.payload)
            }

            challenge { _, _ ->
                call.respond(HttpStatusCode.Unauthorized, "Token is not valid or has expired")
            }
        }
    }

}

enum class AuthenticateName {
    AUTH_JWT
}
