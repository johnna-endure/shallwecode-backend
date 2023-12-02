package shallwecode.kr.auth

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import shallwecode.kr.auth.util.RedirectURLMap
import shallwecode.kr.common.API_CLIENT


fun Application.oauth2GithubRoute() {
    val secret = environment.config.property("oauth.github.secret").getString()
    val clientId = environment.config.property("oauth.github.clientId").getString()
    val redirectURLMap = RedirectURLMap()
//    val authService = AuthService(OAuthGithubPrincipal, LoginHistory)

    authentication {
        oauth("oauth-github") {
            urlProvider = { "http://localhost:8080/authorized/github" }
            providerLookup = {
                OAuthServerSettings.OAuth2ServerSettings(
                    name = "github",
                    authorizeUrl = "https://github.com/login/oauth/authorize",
                    accessTokenUrl = "https://github.com/login/oauth/access_token",
                    requestMethod = HttpMethod.Post,
                    clientId = clientId,
                    clientSecret = secret,
//                    defaultScopes = listOf("https://www.googleapis.com/auth/userinfo.profile")
                    onStateCreated = { call, state ->
//                        println("created state : ${state}")
                        redirectURLMap.save(state, call.request.queryParameters["redirectUrl"])
                    }
                )
            }
            client = API_CLIENT
        }
    }


    routing {
        authenticate("oauth-github") {
            get("/login/github") { }
            get("/authorized/github") {
                val principal = call.principal<OAuthAccessTokenResponse.OAuth2>() ?: return@get call.respond(
                    HttpStatusCode.Unauthorized,
                    "empty principal"
                )
                AuthService.githubLogin(principal, redirectURLMap)


                // TODO 토큰 생성 후 반환


                call.respondRedirect("http://localhost:5173/authorized")
            }
        }
    }

}
