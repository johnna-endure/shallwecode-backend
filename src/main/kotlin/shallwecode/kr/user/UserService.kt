package shallwecode.kr.user

import shallwecode.kr.database.DatabaseFactory
import shallwecode.kr.database.table.UserTable

class UserService {
    suspend fun getLatestGithubAccessToken(userId: Long): String {
        return DatabaseFactory.transactionQuery {
            UserTable.findGithubLatestAccessToken(userId) ?: throw RuntimeException("not found accessToken")
        }
    }

    suspend fun updateGithubUserRepositories() {

    }

    suspend fun getGithubUserRepositories() {
        
    }
}
