package shallwecode.kr.auth

import shallwecode.kr.auth.table.LoginHistory
import shallwecode.kr.auth.table.OAuthGithubPrincipal

class AuthService(
    loginHistory: LoginHistory,
    oauthGithubPrincipal: OAuthGithubPrincipal
) {

    suspend fun githubLogin() {

    }
}
