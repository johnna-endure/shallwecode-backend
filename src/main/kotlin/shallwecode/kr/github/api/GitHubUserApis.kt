package shallwecode.kr.github.api

import io.ktor.client.call.*
import io.ktor.client.request.*
import shallwecode.kr.common.API_CLIENT
import shallwecode.kr.database.table.GithubUserModel


object GitHubUserApis {
    suspend fun getAuthenticatedUser(accessToken: String): GithubUserModel {
        val response = API_CLIENT.get("https://api.github.com/user") {
            this.headers.append("Accept", "application/vnd.github+json")
            this.headers.append("Authorization", "Bearer $accessToken")
            this.headers.append("X-GitHub-Api-Version", "2022-11-28")
        }

        return response.body<GithubUserModel>()
    }
}
