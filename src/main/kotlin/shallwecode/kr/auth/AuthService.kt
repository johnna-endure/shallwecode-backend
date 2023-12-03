package shallwecode.kr.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.*
import io.ktor.server.auth.*
import shallwecode.kr.auth.util.RedirectURLMap
import shallwecode.kr.database.table.LoginAuthType
import shallwecode.kr.database.table.LoginHistoryTable
import shallwecode.kr.database.table.OAuthGithubPrincipalTable
import shallwecode.kr.database.DatabaseFactory
import shallwecode.kr.github.api.GitHubUserApis
import shallwecode.kr.database.table.GithubUserTable
import shallwecode.kr.database.table.UserTable
import java.util.*

class AuthService(
    application: Application
) {
    val secret = application.environment.config.property("jwt.secret").getString()
    val issuer = application.environment.config.property("jwt.issuer").getString()
    val expire = application.environment.config.property("jwt.expire").getString().toInt()

    /**
     * 로그인 정보, 로그인 내역을 저장하고 적합한 사용자일 경우 토큰 발급
     * @return Pair<String, Long> 첫번째 토큰, 두번째 사용자 마스터 아이디
     */
    suspend fun githubLogin(
        principal: OAuthAccessTokenResponse.OAuth2,
        redirectURLMap: RedirectURLMap
    ): String {
        return DatabaseFactory.transactionQuery {
            // 깃허브 인증정보 저장
            val principalId = OAuthGithubPrincipalTable.save(
                stateParam = principal.state!!,
                accessTokenParam = principal.accessToken,
                scopeParam = principal.extraParameters?.get("scope"),
                redirectUriParam = redirectURLMap.getAndRemove(principal.state!!)
            )

            // 액세스 토큰으로 깃허브 사용자 정보 조회하기
            val githubUserInfo = GitHubUserApis.getAuthenticatedUser(principal.accessToken)

            val existGithubUser = GithubUserTable.existById(githubUserInfo.id)
            // 첫 로그인인 경우, 사용자 정보 저장[GithubUserTable, User]
            if (!existGithubUser) {
                GithubUserTable.save(githubUserInfo)
                UserTable.save(
                    emailParam = githubUserInfo.email,
                    nameParam = githubUserInfo.login,
                    githubUserIdParam = githubUserInfo.id
                )
            }
            // 사용자 마스터 테이블 조회, 깃허브 아이디로
            val userId =
                UserTable.findByGithubUserId(githubUserInfo.id)?.let { it.id }
                    ?: throw NoSuchElementException("not founf user.")

            // 로그인 이력 저장
            LoginHistoryTable.save(userId, LoginAuthType.GITHUB, principalId)

            // 토큰 반환
            JWT.create()
                .withIssuer(issuer)
                .withClaim("userId", userId)
                .withExpiresAt(Date(System.currentTimeMillis() + expire))
                .sign(Algorithm.HMAC256(secret))
        }
    }
}
