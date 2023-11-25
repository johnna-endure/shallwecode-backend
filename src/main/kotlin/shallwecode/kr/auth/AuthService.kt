package shallwecode.kr.auth

import io.ktor.server.auth.*
import shallwecode.kr.auth.data.AuthType
import shallwecode.kr.auth.data.LoginHistory
import shallwecode.kr.auth.data.OAuthGithubPrincipal
import shallwecode.kr.database.DatabaseFactory
import javax.xml.crypto.Data

object AuthService {

    /**
     * 로그인 정보, 로그인 내역을 저장하고 적합한 사용자일 경우 토큰 발급
     */
    suspend fun githubLogin(principal: OAuthAccessTokenResponse.OAuth2, redirectURLMap: RedirectURLMap): String {
//        principal 정보 저장하기
//                principal?.state
//                principal?.accessToken
//                principal?.tokenType
//                principal?.expiresIn
//                principal?.refreshToken
//                principal?.extraParameters?.get("scope")
        return DatabaseFactory.transactionQuery {
            // 인증정보 및 이력 저장
            val principalId = OAuthGithubPrincipal.save(
                stateParam = principal.state!!,
                accessTokenParam = principal.accessToken,
                scopeParam = principal.extraParameters?.get("scope"),
                redirectUriParam = redirectURLMap.getAndRemove(principal.state!!)
            )
            LoginHistory.save(AuthType.GITHUB, principalId)


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
