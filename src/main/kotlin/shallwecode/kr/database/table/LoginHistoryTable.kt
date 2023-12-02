package shallwecode.kr.database.table

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime


object LoginHistoryTable : Table() {
    val id = long("id").autoIncrement()
    val authType = varchar("auth_type", length = 10).default("PASSWORD")
    val oauthGithubPrincipalId = long("oauth_github_principal_id").nullable()
    val created = datetime("created").default(LocalDateTime.now())

    override val primaryKey = PrimaryKey(id) // name is optional here
    override val tableName: String = "login_history"
    fun save(authTypeParam: AuthType, oauthId: Long?): Long {
        return insert {
            it[authType] = authTypeParam.name
            it[oauthGithubPrincipalId] = oauthId
        }.resultedValues?.singleOrNull()?.let { it[id] } ?: throw RuntimeException("create failed")
    }
}

@Serializable
data class LoginHistoryModel(
    val id: Long,
    val authType: AuthType,
    val oauthGithubPrincipalId: Long?,
    @Contextual
    val created: LocalDateTime
)

enum class AuthType {
    PASSWORD, GITHUB
}
