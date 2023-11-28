package shallwecode.kr.auth.service

import io.ktor.server.auth.*
import shallwecode.kr.auth.RedirectURLMap
import shallwecode.kr.auth.data.AuthType
import shallwecode.kr.auth.data.LoginHistoryTable
import shallwecode.kr.auth.data.OAuthGithubPrincipalTable
import shallwecode.kr.database.DatabaseFactory

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

            // 첫 로그인인 경우, 사용자 정보 저장

            // 첫 로그인이 아닌 경우, pass


            ""
        }


        // 깃허브 사용자 정보 조회하기
        /*
         깃허브 사용자 정보 저장하기
            - 저장된 정보가 없을 경우 저장, 사용자 마스터 테이블 생성
            - 이미 정보가 있는 경우 패스
         */

        // 로그인을 위한 jwt 토큰 발급
//        return ""

    }
}
