package shallwecode.kr.auth.table

import io.ktor.utils.io.errors.*
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.javatime.datetime
import shallwecode.kr.database.DatabaseFactory
import java.time.LocalDateTime


object LoginHistory : Table() {
    val id = long("id").autoIncrement()
    val authType = varchar("auth_type", length = 10).default("password")
    val oauthGithubPrincipalId = long("oauth_github_principal_id").nullable()
    val created = datetime("created").default(LocalDateTime.now())

    override val primaryKey = PrimaryKey(id) // name is optional here

    suspend fun LoginHistory.create(authTypeParam: AuthType, oauthId: Long?): Long {
        return DatabaseFactory.dbQuery {
            insert {
                it[authType] = authTypeParam.name
                it[oauthGithubPrincipalId] = oauthId
            }.resultedValues?.singleOrNull()?.let { it[id] } ?: throw IOException("create failed")
        }
    }
}

@Serializable
data class LoginHistoryModel(
    val id: Long,
    val authType: AuthType,
    val oauthGithubPrincipalId: Long?,
    @Contextual // Serializer 구현 필요
    val created: LocalDateTime
)

enum class AuthType {
    PASSWORD, GITHUB
}
