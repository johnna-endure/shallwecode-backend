package shallwecode.kr.database.table

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.javatime.datetime
import java.lang.RuntimeException
import java.time.LocalDateTime


object OAuthGithubPrincipalTable : Table("oauth_github_principal") {
    val id = long("id").autoIncrement()
    val state = varchar("state", 100).nullable()
    val accessToken = varchar("access_token", 100)
    val scope = varchar("scope", 50).nullable()
    val redirectUri = varchar("redirect_uri", 100).nullable()
    val created = datetime("created").default(LocalDateTime.now())

    override val primaryKey: PrimaryKey? = PrimaryKey(id)

    fun save(
        stateParam: String?,
        accessTokenParam: String,
        scopeParam: String?,
        redirectUriParam: String?
    ): Long {
        val statement = this.insert {
            it[state] = stateParam
            it[accessToken] = accessTokenParam
            it[scope] = scopeParam
            it[redirectUri] = redirectUriParam
        }

        return statement.resultedValues?.singleOrNull()?.let {
            it[id]
        } ?: throw RuntimeException("crated failed.")
    }

    fun OAuthGithubPrincipalTable.resultRowToModel(resultRow: ResultRow): OAuthGithubPrincipalModel {
        return OAuthGithubPrincipalModel(
            id = resultRow[id],
            state = resultRow[state],
            accessToken = resultRow[accessToken],
            scope = resultRow[scope],
            redirectUri = resultRow[redirectUri],
            created = resultRow[created]
        )
    }

}

data class OAuthGithubPrincipalModel(
    val id: Long,
    val state: String?,
    val accessToken: String,
    val scope: String?,
    val redirectUri: String?,
    val created: LocalDateTime
)
