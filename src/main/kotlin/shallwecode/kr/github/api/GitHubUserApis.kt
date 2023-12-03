package shallwecode.kr.github.api

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import shallwecode.kr.common.API_CLIENT
import shallwecode.kr.database.table.GithubUserModel


object GitHubUserApis {

    /**
     * 깃허브 사용자 정보 조회
     */
    suspend fun getAuthenticatedUser(accessToken: String): GithubUserModel {
        val response = API_CLIENT.get("https://api.github.com/user") {
            this.headers.append("Accept", "application/vnd.github+json")
            this.headers.append("Authorization", "Bearer $accessToken")
            this.headers.append("X-GitHub-Api-Version", "2022-11-28")
        }
        return response.body<GithubUserModel>()
    }

    /**
     * 깃허브 사용자의 리포지토리 조회
     */
    suspend fun getPublicRepositories(accessToken: String) {
        val response = API_CLIENT.get("https://api.github.com/repositories") {
            this.headers.append("Accept", "application/vnd.github+json")
            this.headers.append("Authorization", "Bearer $accessToken")
            this.headers.append("X-GitHub-Api-Version", "2022-11-28")
        }

        println("repositories: ${response.bodyAsText()}")
    }
}
