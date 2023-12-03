package shallwecode.kr.user

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import shallwecode.kr.auth.AuthenticateName
import shallwecode.kr.github.api.GithubRepositoryApis

fun Application.userGithubRoutes() {
    val userService = UserService()

    routing {
        authenticate(AuthenticateName.JWT.name) {
            get("/user/repositories") {
                val principal = call.principal<JWTPrincipal>()
                val userId = principal?.payload?.getClaim("userId")?.asLong()
                    ?: return@get call.respond(HttpStatusCode.Unauthorized, "invalid login token");
                val accessToken = userService.getLatestGithubAccessToken(userId)

                GithubRepositoryApis.getRepositoriesForUser(accessToken)
            }
        }
    }

}
