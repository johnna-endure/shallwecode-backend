package shallwecode.kr.database.table

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime


object LoginHistoryTable : Table("login_history") {
    val id = long("id").autoIncrement()
    val userId = long("user_id") references UserTable.id
    val authType = varchar("auth_type", length = 10).default("PASSWORD")
    val oauthGithubPrincipalId = long("oauth_github_principal_id").nullable()
    val created = datetime("created").default(LocalDateTime.now())

    override val primaryKey = PrimaryKey(id) // name is optional here
    fun save(userIdParam: Long, authTypeParam: LoginAuthType, oauthId: Long?): Long {
        return insert {
            it[userId] = userIdParam
            it[authType] = authTypeParam.name
            it[oauthGithubPrincipalId] = oauthId
        }.resultedValues?.singleOrNull()?.let { it[id] } ?: throw RuntimeException("create failed")
    }
}

@Serializable
data class LoginHistoryModel(
    val id: Long,
    val userId: Long,
    val authType: LoginAuthType,
    val oauthGithubPrincipalId: Long?,
    @Contextual
    val created: LocalDateTime
)

enum class LoginAuthType {
    PASSWORD, GITHUB
}
