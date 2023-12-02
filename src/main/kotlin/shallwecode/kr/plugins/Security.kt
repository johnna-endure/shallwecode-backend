package shallwecode.kr.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import shallwecode.kr.auth.jwt
import shallwecode.kr.auth.oauth2Github

fun Application.configureSecurity() {
    jwt()
    oauth2Github()
}
