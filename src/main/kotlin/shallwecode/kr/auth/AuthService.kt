package shallwecode.kr.auth

import io.ktor.server.auth.*
import shallwecode.kr.auth.util.RedirectURLMap
import shallwecode.kr.database.table.AuthType
import shallwecode.kr.database.table.LoginHistoryTable
import shallwecode.kr.database.table.OAuthGithubPrincipalTable
import shallwecode.kr.database.DatabaseFactory
import shallwecode.kr.github.api.GitHubUserApis
import shallwecode.kr.database.table.GithubUserTable
import shallwecode.kr.database.table.UserTable

object AuthService {

    /**
     * 로그인 정보, 로그인 내역을 저장하고 적합한 사용자일 경우 토큰 발급
     */
    suspend fun githubLogin(principal: OAuthAccessTokenResponse.OAuth2, redirectURLMap: RedirectURLMap): String {
        return DatabaseFactory.transactionQuery {
            // 인증정보 저장
            val principalId = OAuthGithubPrincipalTable.save(
                stateParam = principal.state!!,
                accessTokenParam = principal.accessToken,
                scopeParam = principal.extraParameters?.get("scope"),
                redirectUriParam = redirectURLMap.getAndRemove(principal.state!!)
            )
            // 이력 저장
            LoginHistoryTable.save(AuthType.GITHUB, principalId)

            // 액세스 토큰으로 깃허브 사용자 정보 조회하기
            val githubUserInfo = GitHubUserApis.getAuthenticatedUser(principal.accessToken)

            val existUser = GithubUserTable.existById(githubUserInfo.id)
            // 첫 로그인인 경우, 사용자 정보 저장[GithubUserTable, User]
            if (!existUser) {
                GithubUserTable.save(githubUserInfo)
                UserTable.save(
                    emailParam = githubUserInfo.email,
                    nameParam = githubUserInfo.login,
                    githubUserIdParam = githubUserInfo.id
                )
            }

            // 토큰 반환

            ""
        }
    }
}
