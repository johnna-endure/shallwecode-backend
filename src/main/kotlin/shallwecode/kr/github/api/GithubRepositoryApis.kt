package shallwecode.kr.github.api

import io.ktor.client.request.*
import io.ktor.client.statement.*
import shallwecode.kr.common.API_CLIENT

object GithubRepositoryApis {
    /**
     * 사용자 리포지토리 목록 조회
     */
    suspend fun getRepositoriesForUser(accessToken: String) {
        val response = API_CLIENT.get("https://api.github.com/repositories") {
            this.headers.append("Accept", "application/vnd.github+json")
            this.headers.append("Authorization", "Bearer $accessToken")
            this.headers.append("X-GitHub-Api-Version", "2022-11-28")
        }

        println("repositories: ${response.bodyAsText()}")
    }

}
