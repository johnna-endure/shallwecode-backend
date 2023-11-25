package shallwecode.kr.auth

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import shallwecode.kr.auth.data.LoginHistory
import shallwecode.kr.auth.data.OAuthGithubPrincipal
import shallwecode.kr.client.HTTP_CLIENT


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
                        println("created state : ${state}")
                        redirectURLMap.save(state, call.request.queryParameters["redirectUrl"])
                    }
                )
            }
            client = HTTP_CLIENT
        }
    }


    routing {
        authenticate("oauth-github") {
            get("/login/github") { }
            get("/authorized/github") {
                print("리다이렉트 성공")
                val principal = call.principal<OAuthAccessTokenResponse.OAuth2>() ?: return@get call.respond(
                    HttpStatusCode.Unauthorized,
                    "empty principal"
                )
                AuthService.githubLogin(principal, redirectURLMap)
                // 깃허브 사용자 정보 조회하기

                /*
                 깃허브 사용자 정보 저장하기
                    - 저장된 정보가 없을 경우 저장, 사용자 마스터 테이블 생성
                    - 이미 정보가 있는 경우 패스
                 */

                // 로그인을 위한 jwt 토큰 발급


                call.respondRedirect("http://localhost:5173/authorized")
            }
        }
    }

}
